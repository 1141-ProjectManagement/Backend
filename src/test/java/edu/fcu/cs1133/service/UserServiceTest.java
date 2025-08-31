package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.BadRequestException;
import edu.fcu.cs1133.model.*;
import edu.fcu.cs1133.payload.UserCreationDto;
import edu.fcu.cs1133.payload.UserDto;
import edu.fcu.cs1133.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private StudentProfileRepository studentProfileRepository;
    @Mock
    private TeacherProfileRepository teacherProfileRepository;
    @Mock
    private AdministratorProfileRepository administratorProfileRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ProfileService profileService;

    @InjectMocks
    private UserService userService;

    private User student;
    private Role studentRole;
    private User teacher;
    private Role teacherRole;

    @BeforeEach
    void setUp() {
        studentRole = new Role();
        studentRole.setRoleId(1);
        studentRole.setRoleName("Student");

        student = new User();
        student.setUserId(1);
        student.setOfficialId("student123");
        student.setRole(studentRole);
        student.setEmail("student@test.com");
        student.setIsActive(true);

        teacherRole = new Role();
        teacherRole.setRoleId(2);
        teacherRole.setRoleName("Teacher");

        teacher = new User();
        teacher.setUserId(2);
        teacher.setOfficialId("teacher123");
        teacher.setRole(teacherRole);
        teacher.setEmail("teacher@test.com");
        teacher.setIsActive(true);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(student, teacher));

        List<UserDto> userDtos = userService.getAllUsers();

        assertEquals(2, userDtos.size());
    }

    @Test
    void testGetAllTeachers() {
        when(roleRepository.findByRoleName("Teacher")).thenReturn(Optional.of(teacherRole));
        when(userRepository.findByRole(teacherRole)).thenReturn(Collections.singletonList(teacher));

        List<UserDto> userDtos = userService.getAllTeachers();

        assertEquals(1, userDtos.size());
        assertEquals("teacher123", userDtos.get(0).getOfficialId());
    }

    @Test
    void testCreateUser_Student() {
        UserCreationDto creationDto = new UserCreationDto();
        creationDto.setOfficialId("newStudent");
        creationDto.setEmail("new@student.com");
        creationDto.setPassword("password");
        creationDto.setRoleId(1);
        creationDto.setFirstName("First");
        creationDto.setLastName("Last");

        when(userRepository.existsByOfficialId("newStudent")).thenReturn(false);
        when(userRepository.existsByEmail("new@student.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findById(1)).thenReturn(Optional.of(studentRole));
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setUserId(3);
            return u;
        });

        UserDto createdUser = userService.createUser(creationDto);

        assertEquals("newStudent", createdUser.getOfficialId());
        assertEquals("Student", createdUser.getRoleName());
        verify(studentProfileRepository, times(1)).save(any(StudentProfile.class));
    }

    @Test
    void testCreateUser_OfficialIdExists() {
        UserCreationDto creationDto = new UserCreationDto();
        creationDto.setOfficialId("student123");

        when(userRepository.existsByOfficialId("student123")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            userService.createUser(creationDto);
        });
    }

    @Test
    void testDeleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(student));
        
        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(student);
    }
}