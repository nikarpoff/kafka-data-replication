package students.marks.dal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import students.marks.dal.model.Mark;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MarkRepository extends CrudRepository<Mark, Long> {

    @Query("SELECT m.markPK.student.id, m.markPK.lab.labNum, m.value FROM Mark m")
    List<Object[]> findAllStudentsWithMarksOnOneSubject();

    void deleteAllByMarkPKLabLabNum(int labNum);

}
