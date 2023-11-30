package students.marks.dal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * object with info about subject
 */
@Table(name = "lab_work")
@Entity
@Data
public class LabWork {

  @OneToMany(mappedBy = "markPK.lab", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Mark> marks;

  @JsonProperty("lab_num")
  @Id
  @Column(name = "lab_num", unique = true)
  private int labNum = 0;

  @Override
  public String toString() {
    return "LabWork{" +
            "labNum=" + labNum +
            '}';
  }

  public List<Mark> getMarks() {
    if (marks == null) {
      marks = new ArrayList<>();
    }
    return marks;
  }
}
