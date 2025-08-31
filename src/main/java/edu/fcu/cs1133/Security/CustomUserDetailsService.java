package edu.fcu.cs1133.security;

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

    return UserPrincipal.create(user);
  }
}