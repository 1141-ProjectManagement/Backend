package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.BadRequestException;
import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.*;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.UserCreationDto;
import edu.fcu.cs1133.payload.UserDto;
import edu.fcu.cs1133.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private AdministratorProfileRepository administratorProfileRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileService profileService;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllTeachers() {
        Role teacherRole = roleRepository.findByRoleName("Teacher")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "Teacher"));
        return userRepository.findByRole(teacherRole).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllAdmins() {
        Role adminRole = roleRepository.findByRoleName("Admin")
                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", "Admin"));
        return userRepository.findByRole(adminRole).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToDto(user);
    }

    @Transactional
    public UserDto createUser(UserCreationDto userDto) {
        if (userRepository.existsByOfficialId(userDto.getOfficialId())) {
            throw new BadRequestException("Official ID is already taken!");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("Email is already in use!");
        }

        User user = new User();
        user.setOfficialId(userDto.getOfficialId());
        user.setPasswordHash(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setCreatedAt(LocalDate.now());
        user.setIsActive(true);

        Role userRole = roleRepository.findById(userDto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", userDto.getRoleId()));
        user.setRole(userRole);

        User savedUser = userRepository.save(user);

        if ("Student".equals(userRole.getRoleName())) {
            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setUser(savedUser);
            studentProfile.setUserId(savedUser.getUserId());
            studentProfile.setFirstName(userDto.getFirstName());
            studentProfile.setLastName(userDto.getLastName());
            studentProfileRepository.save(studentProfile);
        } else if ("Teacher".equals(userRole.getRoleName())) {
            edu.fcu.cs1133.model.TeacherProfile teacherProfile = new edu.fcu.cs1133.model.TeacherProfile();
            teacherProfile.setUser(savedUser);
            teacherProfile.setUserId(savedUser.getUserId());
            teacherProfile.setFullName(userDto.getFullName());
            teacherProfileRepository.save(teacherProfile);
        } else if ("Admin".equals(userRole.getRoleName())) {
            edu.fcu.cs1133.model.AdministratorProfile adminProfile = new edu.fcu.cs1133.model.AdministratorProfile();
            adminProfile.setUser(savedUser);
            adminProfile.setUserId(savedUser.getUserId());
            adminProfile.setFullName(userDto.getFullName());
            administratorProfileRepository.save(adminProfile);
        }

        return convertToDto(savedUser);
    }

    @Transactional
    public UserDto updateStudentProfile(Integer userId, ProfileUpdateDto profileDto) {
        profileService.updateStudentProfile(userId, profileDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToDto(user);
    }

    @Transactional
    public UserDto updateTeacherProfile(Integer userId, ProfileUpdateDto profileDto) {
        profileService.updateTeacherProfile(userId, profileDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToDto(user);
    }

    @Transactional
    public UserDto updateAdminProfile(Integer userId, ProfileUpdateDto profileDto) {
        profileService.updateAdminProfile(userId, profileDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToDto(user);
    }

    // TODO: updateAdminProfile

    @Transactional
    public void deleteUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Manually delete related entities
        enrollmentRepository.deleteAll(enrollmentRepository.findByStudentUserId(userId));
        studentProfileRepository.findById(userId).ifPresent(studentProfileRepository::delete);
        teacherProfileRepository.findById(userId).ifPresent(teacherProfileRepository::delete);
        administratorProfileRepository.findById(userId).ifPresent(administratorProfileRepository::delete);

        userRepository.delete(user);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setOfficialId(user.getOfficialId());
        userDto.setEmail(user.getEmail());
        userDto.setActive(user.getIsActive());
        if (user.getRole() != null) {
            userDto.setRoleName(user.getRole().getRoleName());
        }
        return userDto;
    }
}