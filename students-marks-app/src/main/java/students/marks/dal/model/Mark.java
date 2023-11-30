package students.marks.dal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * object with info about student's mark
 */
@Table(name = "mark")
@Entity
@Data
public class Mark   {

  @EmbeddedId
  MarkPK markPK = new MarkPK();

  @JsonProperty("value")
  private int value = 0;


  public Student getStudent() {
    return markPK.getStudent();
  }

  public void setStudent(Student studentId) {
    this.markPK.setStudent(studentId);
  }

  public LabWork getLab() {
    return markPK.getLab();
  }

  public void setLab(LabWork lab) {
    this.markPK.setLab(lab);
  }

}
