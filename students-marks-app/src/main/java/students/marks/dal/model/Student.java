package students.marks.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Table(name = "student")
@Entity
@Data
public class Student   {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  @OneToMany(mappedBy = "markPK.student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Mark> marks;

  @Override
  public String toString() {
    return "Student{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
  }

  public List<Mark> getMarks() {
    if (marks == null) {
      marks = new ArrayList<>();
    }
    return marks;
  }
}
