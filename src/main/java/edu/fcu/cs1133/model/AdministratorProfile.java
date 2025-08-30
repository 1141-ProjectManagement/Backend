package edu.fcu.cs1133.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Administrator_Profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AdministratorProfile {
  @Id // 1. 宣告為主鍵
  @Column(name = "user_id")
  private Integer userId;

  @OneToOne(fetch = FetchType.LAZY) // 2. 建立一對一關係
  @MapsId // 3. 核心！將主鍵映射到關聯物件的 ID
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  // --- Constructors, Getters, and Setters ---
  public AdministratorProfile(User user, String fullName, String email) {
    this.user = user;
    this.fullName = fullName;
  }

  public Integer getUserId() { return userId; }
  public void setUserId(Integer userId) { this.userId = userId; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }
}