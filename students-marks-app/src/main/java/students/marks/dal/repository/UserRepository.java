package students.marks.dal.repository;

import org.springframework.data.repository.CrudRepository;
import students.marks.dal.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);

}
