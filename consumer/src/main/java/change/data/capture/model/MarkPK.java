package change.data.capture.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarkPK implements Serializable {

    @ManyToOne
    @JsonProperty("student")
    private Student student;

    @ManyToOne
    private LabWork lab;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarkPK markPK = (MarkPK) o;
        return Objects.equals(student, markPK.student) && Objects.equals(lab, markPK.lab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(student, lab);
    }
}
