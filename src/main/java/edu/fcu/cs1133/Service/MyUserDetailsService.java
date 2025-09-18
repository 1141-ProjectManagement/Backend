package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.repository.StudentsRepository;
import edu.fcu.cs1133.repository.TeachersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private StudentsRepository studentsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return teachersRepository.findByUsername(username)
                .map(UserDetails.class::cast)
                .orElseGet(() -> studentsRepository.findByUsername(username)
                        .map(UserDetails.class::cast)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username)));
    }
}
