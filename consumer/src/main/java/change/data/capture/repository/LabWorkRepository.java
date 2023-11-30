package change.data.capture.repository;

import change.data.capture.model.LabWork;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LabWorkRepository extends CrudRepository<LabWork, Integer> {

    void deleteByLabNum(int labNum);

    Optional<LabWork> findLabWorkByLabNum(int labNum);

}
