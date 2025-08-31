package edu.fcu.cs1133.model;

import jakarta.persistence.*;
import java.util.Set;

//public enum Role {
//    ADMIN,
//    TEACHER,
//    STUDENT
//}

@Entity
@Table(name = "Roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Integer roleId;

  @Column(name = "role_name", unique = true, nullable = false)
  private String roleName;

  private String description;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "Role_Permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id")
  )
  private Set<Permission> permissions;

// Getters and Setters...
  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }
}
// Compare this snippet from src/main/java/edu/fcu/cs1133/model/Permission.java: