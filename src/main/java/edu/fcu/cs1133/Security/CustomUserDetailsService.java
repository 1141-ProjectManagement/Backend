package edu.fcu.cs1133.Security;

import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional // 建議加上事務註解
  public UserDetails loadUserByUsername(String officialId) throws UsernameNotFoundException {
    // 雖然方法參數名是 username，但它實際接收的是用戶登入時提交的識別碼，在我們的系統中就是 official_id
    User user = userRepository.findByOfficialId(officialId)
        .orElseThrow(() ->
            new UsernameNotFoundException("User not found with official_id: " + officialId)
        );

    return new org.springframework.security.core.userdetails.User(
        user.getOfficialId(), // Principal: 使用 official_id 作為 Spring Security 內部的 "username"
        user.getPasswordHash(), // Credentials: 密碼
        user.isActive(), // isEnabled: 帳號是否啟用
        true, // isAccountNonExpired
        true, // isCredentialsNonExpired
        true, // isAccountNonLocked
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName())) // Authorities: 權限
    );
  }
}