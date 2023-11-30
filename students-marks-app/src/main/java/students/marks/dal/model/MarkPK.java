package students.marks.dal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Data
public class MarkPK implements Serializable {

    @ManyToOne
    private Student student;

    @ManyToOne
    private LabWork lab;

}
