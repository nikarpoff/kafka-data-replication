package change.data.capture.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * object with info about student's mark
 */
@Table(name = "mark")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mark   {

  @EmbeddedId
  MarkPK markPK = new MarkPK();

  private int value = 0;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Mark mark = (Mark) o;
    return value == mark.value && Objects.equals(markPK, mark.markPK);
  }

  @Override
  public int hashCode() {
    return Objects.hash(markPK, value);
  }
}
