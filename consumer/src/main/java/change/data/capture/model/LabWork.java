package change.data.capture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * object with info about subject
 */
@Table(name = "lab_work")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LabWork labWork = (LabWork) o;
    return labNum == labWork.labNum && Objects.equals(marks, labWork.marks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(marks, labNum);
  }
}
