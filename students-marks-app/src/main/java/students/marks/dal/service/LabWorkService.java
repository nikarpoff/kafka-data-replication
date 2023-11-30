package students.marks.dal.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import students.marks.dal.repository.LabWorkRepository;
import students.marks.dal.repository.MarkRepository;
import students.marks.dal.repository.StudentRepository;
import students.marks.dal.exception.DatabaseException;
import students.marks.dal.model.LabWork;
import students.marks.dal.model.Mark;
import students.marks.dal.model.Student;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LabWorkService implements IService<LabWork> {
    private final MarkRepository markRepository;
    private final LabWorkRepository labWorkRepository;

    @Override
    public Iterable<LabWork> listAll() {
        return labWorkRepository.findAll();
    }

    @Override
    public void delete(Long id) throws DatabaseException {

    }

    @Transactional
    public void deleteByLabNum(int labNum) throws DatabaseException {
        try {
            labWorkRepository.deleteByLabNum(labNum);
            markRepository.deleteAllByMarkPKLabLabNum(labNum);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public LabWork add(LabWork labWork) throws DatabaseException {
        try {
            return labWorkRepository.save(labWork);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public LabWork update(LabWork labWork) throws DatabaseException {
        return null;
    }

    @Override
    public LabWork findById(Long id) throws DatabaseException {
        return null;
    }

    public LabWork findByLabNum(int labNum) throws DatabaseException {
        Optional<LabWork> labWork = labWorkRepository.findById(labNum);

        if (labWork.isEmpty()) {
            throw new DatabaseException("lab work was not found");
        }

        return labWork.get();
    }

}
