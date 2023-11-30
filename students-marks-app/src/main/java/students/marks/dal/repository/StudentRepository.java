package students.marks.dal.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import students.marks.dal.model.Student;


@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

}
