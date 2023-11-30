package change.data.capture.kafka;

import javax.transaction.Transactional;

import change.data.capture.model.LabWork;
import change.data.capture.model.Mark;
import change.data.capture.model.MarkPK;
import change.data.capture.model.Student;
import change.data.capture.repository.LabWorkRepository;
import change.data.capture.repository.MarkRepository;
import change.data.capture.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.hibernate.StaleStateException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.apache.avro.util.Utf8;
import org.apache.avro.generic.GenericData.Record;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaConsumerListeners {

    LabWorkRepository labWorkRepository;
    MarkRepository markRepository;
    StudentRepository studentRepository;

    private final String LAB_NUMBER_FIELD = "lab_num";
    private final String MARK_LAB_NUMBER_FIELD = "lab_lab_num";
    private final String MARK_STUDENT_ID_FIELD = "student_id";
    private final String MARK_VALUE_FIELD = "value";
    private final String STUDENT_ID_FIELD = "id";
    private final String STUDENT_NAME_FIELD = "name";

    // fields-keys of Avro deserializer
    private final String OPERATION_TYPE_KEY = "op";
    private final String AFTER_KEY = "after";
    private final String OPERATION_DELETE_KEY = "d";

    /** Consumer for topic about lab (add/delete lab from table from target db) */
    @Transactional
    @KafkaListener(topics = "${kafka.topic_lab}", groupId = "${kafka.groupId}")
    public void debeziumListenerLab(ConsumerRecord<org.apache.avro.generic.GenericData.Record, org.apache.avro.generic.GenericData.Record> record) {

        log.info("Consumer listener lab work got request!");

        if (record.value() == null) {
            return;
        }

        String op = ((Utf8) record.value().get(OPERATION_TYPE_KEY)).toString();

        //DELETING
        if (op.equals(OPERATION_DELETE_KEY)) {
            Integer labNum = (Integer) record.key().get(LAB_NUMBER_FIELD);
            try {
                labWorkRepository.deleteByLabNum(labNum);
            } catch (Exception ignored) {
                // Пропуск удаления, так как данные уже были удалены ввиду появления нового изменения в логах репликации (происходит зацикливание)
            }
            return;
        }

        // ADDING
        Record value = (Record) record.value().get(AFTER_KEY);
        Integer labNum = (Integer) value.get(LAB_NUMBER_FIELD);

        Optional<LabWork> foundedLab = labWorkRepository.findLabWorkByLabNum(labNum);

        if (foundedLab.isEmpty()) {
            LabWork labWork = new LabWork(null, labNum);
            labWorkRepository.save(labWork);
            log.info("Operation is adding! Lab " + labNum + " was added!");
        } else {
            log.info("Lab " + labNum + " already exists! Skipping adding!");
        }
    }

    /** Consumer for topic about student (add/delete student from table from target db) */
    @Transactional
    @KafkaListener(topics = "${kafka.topic_student}", groupId = "${kafka.groupId}")
    public void debeziumListenerStudent(ConsumerRecord<org.apache.avro.generic.GenericData.Record, org.apache.avro.generic.GenericData.Record> record) {

        log.info("Consumer listener student got request!");

        if (record.value() == null) {
            return;
        }

        String op = ((Utf8) record.value().get(OPERATION_TYPE_KEY)).toString();

        //DELETING
        if (op.equals(OPERATION_DELETE_KEY)) {
            Long studentId = (Long) record.key().get(STUDENT_ID_FIELD);
            try {
                studentRepository.deleteById(studentId);
            } catch (Exception ignored) {
                // Пропуск удаления, так как данные уже были удалены ввиду появления нового изменения в логах репликации (происходит зацикливание)
                // EmptyResultDataAccessException | StaleStateException ignore
            }
            return;
        }

        // ADDING
        Record value = (Record) record.value().get(AFTER_KEY);
        Long studentId = (Long) value.get(STUDENT_ID_FIELD);
        String studentName = ((Utf8) value.get(STUDENT_NAME_FIELD)).toString();

        log.info("name: " + studentName);
        log.info("id: " + studentId);

        Optional<Student> foundedStudent = studentRepository.findById(studentId);

        if (foundedStudent.isEmpty()) {
            Student student = new Student(studentId, studentName, null);
            log.info(student.toString());
            studentRepository.save(student);
            log.info("Operation is adding! Student " + student.toString() + " was added!");
        } else {
            log.info("Student " + foundedStudent.get().toString() + " already exists! Skipping adding!");
        }
    }

    /** Consumer for topic about marks (add/delete/update mark from table from target db) */
    @Transactional
    @KafkaListener(topics = "${kafka.topic_mark}", groupId = "${kafka.groupId}")
    public void debeziumListenerMark(ConsumerRecord<org.apache.avro.generic.GenericData.Record, org.apache.avro.generic.GenericData.Record> record) {

        log.info("Consumer listener mark got request!");

        if (record.value() == null) {
            return;
        }

        String op = ((Utf8) record.value().get(OPERATION_TYPE_KEY)).toString();

        log.info(record.key().toString());

        //DELETING
        if (op.equals(OPERATION_DELETE_KEY)) {
            int labNum = (Integer) record.key().get(MARK_LAB_NUMBER_FIELD);
            Long studentId = (Long) record.key().get(MARK_STUDENT_ID_FIELD);

            try {
                markRepository.deleteByMarkPKLabLabNumAndMarkPKStudentId(labNum, studentId);
            } catch (Exception ignored) {
                // Пропуск удаления, так как данные уже были удалены ввиду появления нового изменения в логах репликации (происходит зацикливание)
            }

            return;
        }

        // ADDING OR UPDATING
        Record value = (Record) record.value().get(AFTER_KEY);

        int labNum = (Integer) record.key().get(MARK_LAB_NUMBER_FIELD);
        Long studentId = (Long) record.key().get(MARK_STUDENT_ID_FIELD);
        int markValue = (Integer) value.get(MARK_VALUE_FIELD);

        log.info(markValue + "");

        Optional<Mark> foundedMark = markRepository.findByMarkPKLabLabNumAndMarkPKStudentId(labNum, studentId);
        Mark mark;

        if (foundedMark.isEmpty()) {
            Student markedStudent = new Student();
            LabWork markedLab = new LabWork();
            markedStudent.setId(studentId);
            markedLab.setLabNum(labNum);

            mark = new Mark(new MarkPK(markedStudent, markedLab), markValue);
            log.info("Mark " + mark.toString() + " was added!");
        } else {
            // Мы можем вызвать сохранение, изменив поле оценки. Даже если данные окажутся те же,
            // запись в логах изменений не появится и зацикливания обновлений не произойдёт (как же он ошибался! всё-всё в логах появится)
            mark = foundedMark.get();

            log.info(markValue + " " + mark.getValue());


            if (mark.getValue() == markValue) {
                log.info("Mark " + mark.toString() + " already updated!");
                return;
            }

            mark.setValue(markValue);
        }

        markRepository.save(mark);
    }
}
