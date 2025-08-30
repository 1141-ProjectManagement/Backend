package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Teacher_Profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class TeacherProfile {
  @Id
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(name = "age")
  private Integer age;

//  Constructors, Getters, and Setters
  public TeacherProfile(User user, String fullName, Integer age, String email) {
    this.user = user;
    this.fullName = fullName;
    this.age = age;
  }

  public Integer getUserId() { return userId; }
  public void setUserId(Integer userId) { this.userId = userId; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
  public Integer getAge() { return age; }
  public void setAge(Integer age) { this.age = age; }
}