package edu.fcu.cs1133.payload;
import java.time.LocalDate;
// Getters, Setters, Constructors...
public class EnrollmentDto {
  private String courseName;
  private Integer credits;
  private LocalDate enrollmentDate;
  private String grade;

  public String getCourseName() {
    return courseName;
  }

  public void setCourseName(String courseName) {
    this.courseName = courseName;
  }

  public Integer getCredits() {
    return credits;
  }

  public void setCredits(Integer credits) {
    this.credits = credits;
  }

  public LocalDate getEnrollmentDate() {
    return enrollmentDate;
  }

  public void setEnrollmentDate(LocalDate enrollmentDate) {
    this.enrollmentDate = enrollmentDate;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }
// ... 可以添加更多需要的欄位
}