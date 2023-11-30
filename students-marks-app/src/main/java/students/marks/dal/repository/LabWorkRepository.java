package students.marks.dal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import students.marks.dal.model.LabWork;

import javax.transaction.Transactional;

@Repository
public interface LabWorkRepository extends CrudRepository<LabWork, Integer> {

    void deleteByLabNum(int labNum);

}
