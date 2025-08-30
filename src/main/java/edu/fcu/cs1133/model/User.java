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

// User.java
@Entity
@Table(name = "Users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash; // 儲存雜湊後的密碼

  @Column(unique = true, nullable = false)
  private String email;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "last_login")
  private LocalDateTime lastLogin;

  @Column(name = "is_active")
  private boolean isActive;

  @ManyToOne(fetch = FetchType.EAGER) // 登入時通常需要立即知道角色
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  // --- 這是與 Profile 的雙向關聯 ---
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private StudentProfile studentProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private TeacherProfile teacherProfile;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private AdministratorProfile administratorProfile;


  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    isActive = true;
  }

// --- Constructors, Getters, and Setters ---
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(LocalDateTime lastLogin) {
    this.lastLogin = lastLogin;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setActive(boolean active) {
    isActive = active;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
  public StudentProfile getStudentProfile() {
    return studentProfile;
  }
  public void setStudentProfile(StudentProfile studentProfile) {
    this.studentProfile = studentProfile;
  }
  public TeacherProfile getTeacherProfile() {
    return teacherProfile;
  }
  public void setTeacherProfile(TeacherProfile teacherProfile) {
    this.teacherProfile = teacherProfile;
  }
  public AdministratorProfile getAdministratorProfile() {
    return administratorProfile;
  }
  public void setAdministratorProfile(AdministratorProfile administratorProfile) {
    this.administratorProfile = administratorProfile;
  }
}
