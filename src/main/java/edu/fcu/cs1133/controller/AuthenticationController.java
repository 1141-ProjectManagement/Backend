package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.Service.MyUserDetailsService;
import edu.fcu.cs1133.Service.StudentService;
import edu.fcu.cs1133.Service.TeacherService;
import edu.fcu.cs1133.model.*;
import edu.fcu.cs1133.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsersForLogin() {
        List<UserDto> teachers = teacherService.getAllTeachers().stream()
                .filter(t -> t.getUsername() != null && !t.getUsername().isEmpty())
                .map(t -> {
                    String role = t.getRole() != null ? t.getRole().name() : "N/A";
                    return new UserDto(t.getTeacherId(), t.getUsername(), t.getName(), role);
                })
                .collect(Collectors.toList());

        List<UserDto> students = studentService.getAllStudents().stream()
                .filter(s -> s.getUsername() != null && !s.getUsername().isEmpty())
                .map(s -> {
                    String role = s.getRole() != null ? s.getRole().name() : "N/A";
                    String displayName = (s.getFirstName() != null ? s.getFirstName() : "") + " " + (s.getLastName() != null ? s.getLastName() : "");
                    return new UserDto(s.getStudentId(), s.getUsername(), displayName.trim(), role);
                })
                .collect(Collectors.toList());

        return Stream.concat(teachers.stream(), students.stream()).collect(Collectors.toList());
    }

    @PostMapping("/login-as/{username}")
    public ResponseEntity<?> createAuthenticationTokenAs(@PathVariable String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/login-as-id")
    public ResponseEntity<?> createAuthenticationTokenById(@RequestBody LoginAsIdRequest request) {
        UserDetails userDetails;
        if ("STUDENT".equals(request.getRole())) {
            userDetails = studentService.getStudentById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
        } else if ("TEACHER".equals(request.getRole()) || "ADMIN".equals(request.getRole())) {
            userDetails = teacherService.getTeacherById(request.getId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
        } else {
            throw new RuntimeException("Invalid role");
        }

        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}