package edu.fcu.cs1133.Security;

import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String officialId) throws UsernameNotFoundException {
    User user = userRepository.findByOfficialId(officialId)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with official_id: " + officialId)
        );

    // 核心改動：從 Role 實體中獲取所有關聯的 Permission
    // 並將 permission_name 作為 GrantedAuthority
    Set<GrantedAuthority> authorities = user.getRole().getPermissions().stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
            .collect(Collectors.toSet());

    // 額外加上角色本身作為一個 Authority，這是 Spring Security 的慣例
    authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName()));

    return new org.springframework.security.core.userdetails.User(
        user.getOfficialId(), // Principal: 使用 official_id 作為 Spring Security 內部的 "username"
        user.getPasswordHash(), // Credentials: 密碼
        user.getIsActive(), // isEnabled: 帳號是否啟用
        true, // isAccountNonExpired
        true, // isCredentialsNonExpired
        true, // isAccountNonLocked
        authorities // <-- 使用包含所有權限的 Set
    );
  }
}