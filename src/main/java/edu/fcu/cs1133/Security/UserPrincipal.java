package edu.fcu.cs1133.security;

import edu.fcu.cs1133.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

  private final Integer id; // <-- 我們需要這個！
  private final String officialId;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public UserPrincipal(Integer id, String officialId, String password, Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.officialId = officialId;
    this.password = password;
    this.authorities = authorities;
  }

  public static UserPrincipal create(User user) {
    // 從 Role 實體中獲取所有關聯的 Permission，並將 permission_name 作為 GrantedAuthority
    Collection<GrantedAuthority> authorities = user.getRole().getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
            .collect(Collectors.toSet());

    // 額外加上角色本身作為一個 Authority
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

    return new UserPrincipal(
        user.getUserId(),
        user.getOfficialId(),
        user.getPasswordHash(),
        authorities
    );
  }

  public Integer getId() { // <-- 提供 ID 的 getter
    return id;
  }

  @Override
  public String getUsername() {
    return officialId; // UserDetails 的 getUsername() 返回我們的唯一識別碼
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true; // Or map this to a field in your User entity
  }

  // equals() and hashCode() a good practice to implement
}