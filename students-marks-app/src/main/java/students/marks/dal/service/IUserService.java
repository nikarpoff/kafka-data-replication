package students.marks.dal.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import students.marks.dal.model.User;

public interface IUserService {

    public User loadUserByUsername(String username) throws UsernameNotFoundException;

}
