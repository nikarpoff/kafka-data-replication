package students.marks.front.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import students.marks.dal.service.UserService;
import students.marks.front.model.MyUserDetails;
import students.marks.dal.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {

    final UserService userService;

    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userService.loadUserByUsername(login);
        return new MyUserDetails(user);
    }
}
