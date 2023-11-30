package students.marks.dal.model;


import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "usr")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "pass_hash", nullable = false)
    private String passHash;

    @Column(name = "active")
    private boolean active;

    @Column(name = "roles")
    private String roles;

}
