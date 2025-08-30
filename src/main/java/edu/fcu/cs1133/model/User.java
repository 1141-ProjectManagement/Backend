package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import edu.fcu.cs1133.model.Role;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

// User.java

@Entity
@Table(name = "Users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "official_id", unique = true, nullable = false)
  private String officialId; // <--- 關鍵變更

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  // ... 其他欄位
  // email
  // role_id
  // created_at
//  last_login
//      is_active
  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", nullable = false)
  private Role role;

  @Column(name = "created_at", nullable = false)
  private LocalDate createdAt;
  @Column(name = "last_login")
  private LocalDate lastLogin;
  @Column(name = "is_active", nullable = false)
  private Boolean isActive;

  // --- Getters and Setters for officialId ---
  public String getOfficialId() {
    return officialId;
  }

  public void setOfficialId(String officialId) {
    this.officialId = officialId;
  }

  // ... 其他 Getters and Setters
  public Integer getUserId() { return userId; }
  public void setUserId(Integer userId) { this.userId = userId; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }
  public LocalDate getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
  public LocalDate getLastLogin() { return lastLogin; }
  public void setLastLogin(LocalDate lastLogin) { this.lastLogin = lastLogin; }
  public Boolean getIsActive() { return isActive; }
  public void setIsActive(Boolean isActive) { this.isActive = isActive; }
  // --- End of Getters and Setters ---
}
