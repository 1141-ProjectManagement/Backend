package edu.fcu.cs1133.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EnrollmentId implements Serializable {
  @Column(name = "student_id")
  private Integer studentId;
  @Column(name = "course_id")
  private Integer courseId;
  // --- Constructor, Getters, Setters, equals, and hashCode ---
  public EnrollmentId() {}
  public EnrollmentId(Integer studentId, Integer courseId) {
    this.studentId = studentId;
    this.courseId = courseId;
  }
  public Integer getStudentId() { return studentId; }
  public void setStudentId(Integer studentId) { this.studentId = studentId; }
  public Integer getCourseId() { return courseId; }
  public void setCourseId(Integer courseId) { this.courseId = courseId; }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EnrollmentId that = (EnrollmentId) o;
    return Objects.equals(studentId, that.studentId) && Objects.equals(courseId, that.courseId);
  }
  @Override
  public int hashCode() {
    return Objects.hash(studentId, courseId);
  }
}