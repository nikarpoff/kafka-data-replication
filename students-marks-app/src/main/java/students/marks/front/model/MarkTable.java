package students.marks.front.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MarkTable   {

  @JsonProperty("student_id")
  private Long studentId = null;

  @JsonProperty("lab_num")
  private int labNum = 0;

  @JsonProperty("value")
  private int value = 0;


  public MarkTable studentId(Long studentId) {
    this.studentId = studentId;
    return this;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  public MarkTable labNum(int labNum) {
    this.labNum = labNum;
    return this;
  }

  public int getLabNum() {
    return labNum;
  }

  public void setLabNum(int labNum) {
    this.labNum = labNum;
  }

  public MarkTable value(int value) {
    this.value = value;
    return this;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarkTable markTable = (MarkTable) o;
    return
        Objects.equals(this.studentId, markTable.studentId) &&
        Objects.equals(this.labNum, markTable.labNum) &&
        Objects.equals(this.value, markTable.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(studentId, labNum, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarkTable {\n");

    sb.append("    studentId: ").append(toIndentedString(studentId)).append("\n");
    sb.append("    labNum: ").append(toIndentedString(labNum)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
