package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Enrollment")
public class Enrollment {

  /**
   * 使用 @EmbeddedId 註解來宣告一個嵌入式複合主鍵。
   */
  @EmbeddedId
  private EnrollmentId id;

  /**
   * 與 User 的多對一關聯。
   * @MapsId("studentId") 是一個關鍵註解，它告訴 JPA：
   * "這個 student 關聯的外鍵欄位 (student_id)，同時也是我 @EmbeddedId 物件中 studentId 屬性的值來源。"
   * 這樣可以確保資料庫中只有一個 student_id 欄位，避免了冗餘。
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("studentId") // 映射到 EnrollmentId 中的 "studentId" 屬性
  @JoinColumn(name = "student_id")
  private User student;

  /**
   * 與 Course 的多對一關聯。
   * @MapsId("courseId") 同理，將外鍵 course_id 映射到 id 物件中的 courseId 屬性。
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("courseId") // 映射到 EnrollmentId 中的 "courseId" 屬性
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "enrollment_date", nullable = false)
  private LocalDate enrollmentDate;

  @Column(name = "grade")
  private String grade;

  // --- Constructors, Getters, and Setters ---
  public Enrollment() {}

  public EnrollmentId getId() {
    return id;
  }

  public void setId(EnrollmentId id) {
    this.id = id;
  }

  public User getStudent() {
    return student;
  }

  public void setStudent(User student) {
    this.student = student;
  }

  public Course getCourse() {
    return course;
  }

  public void setCourse(Course course) {
    this.course = course;
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
}