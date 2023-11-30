package change.data.capture.repository;

import change.data.capture.model.Mark;
import change.data.capture.model.MarkPK;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarkRepository extends CrudRepository<Mark, Long> {
    void deleteAllByMarkPKLabLabNum(int labNum);

    void deleteByMarkPKLabLabNumAndMarkPKStudentId(int labNum, Long studentId);

    Optional<Mark> findByMarkPKLabLabNumAndMarkPKStudentId(int labNum, Long studentId);
}
