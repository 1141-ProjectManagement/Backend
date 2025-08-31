package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "course_id")
  private Integer courseId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "teacher_id")
  private TeacherProfile teacher;

  @Column(name = "course_name", nullable = false)
  private String courseName;

  @Column(name = "course_description")
  private String courseDescription;

  @Column(name = "credits", nullable = false)
  private Integer credits;
}
