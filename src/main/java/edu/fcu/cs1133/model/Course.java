package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Courses")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Course {
//  course_id
//      teacher_id
//  course_name
//      course_description
//  credits
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Integer courseId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id", nullable = false)
  private TeacherProfile teacher;

  @Column(name = "course_name", nullable = false)
  private String courseName;

  @Column(name = "course_description")
  private String courseDescription;

  @Column(name = "credits", nullable = false)
  private Integer credits;

//  Constructors, Getters, and Setters
  public Course(TeacherProfile teacher, String courseName, String courseDescription, Integer credits) {
    this.teacher = teacher;
    this.courseName = courseName;
    this.courseDescription = courseDescription;
    this.credits = credits;
  }

  public Integer getCourseId() { return courseId; }
  public void setCourseId(Integer courseId) { this.courseId = courseId; }
  public TeacherProfile getTeacher() { return teacher; }
  public void setTeacher(TeacherProfile teacher) { this.teacher = teacher; }
  public String getCourseName() { return courseName; }
  public void setCourseName(String courseName) { this.courseName = courseName; }
  public String getCourseDescription() { return courseDescription; }
  public void setCourseDescription(String courseDescription) { this.courseDescription = courseDescription; }
  public Integer getCredits() { return credits; }
  public void setCredits(Integer credits) { this.credits = credits; }
}