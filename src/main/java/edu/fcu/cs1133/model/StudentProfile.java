package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Student_Profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class StudentProfile {

  @Id // 1. 宣告為主鍵
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY) // 2. 建立一對一關係
  @MapsId // 3. 核心！將主鍵映射到關聯物件的 ID
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "date_of_birth")
  private java.time.LocalDate dateOfBirth;

  // --- Constructors, Getters, and Setters ---
  public StudentProfile(User user, String firstName, String lastName, java.time.LocalDate dateOfBirth) {
    this.user = user;
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateOfBirth = dateOfBirth;
  }
  public Integer getUserId() { return userId; }
  public void setUserId(Integer userId) { this.userId = userId; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }
  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }
  public java.time.LocalDate getDateOfBirth() { return dateOfBirth; }
  public void setDateOfBirth(java.time.LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
}