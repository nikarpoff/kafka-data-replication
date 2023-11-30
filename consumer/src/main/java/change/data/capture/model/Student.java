package change.data.capture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Table(name = "student")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student   {

  @Id
  @JsonProperty("student_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id = null;

  @JsonProperty("student_name")
  private String name = null;

  @OneToMany(mappedBy = "markPK.student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Mark> marks;

  @Override
  public String toString() {
    return "Student{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
  }
}
