package edu.fcu.cs1133.service;

import edu.fcu.cs1133.model.AdministratorProfile;
import edu.fcu.cs1133.model.StudentProfile;
import edu.fcu.cs1133.model.TeacherProfile;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.repository.AdministratorProfileRepository;
import edu.fcu.cs1133.repository.StudentProfileRepository;
import edu.fcu.cs1133.repository.TeacherProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private StudentProfileRepository studentProfileRepository;

    @Mock
    private TeacherProfileRepository teacherProfileRepository;

    @Mock
    private AdministratorProfileRepository administratorProfileRepository;

    @InjectMocks
    private ProfileService profileService;

    private StudentProfile studentProfile;
    private TeacherProfile teacherProfile;
    private AdministratorProfile adminProfile;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setOfficialId("testuser");
        user.setEmail("test@test.com");

        studentProfile = new StudentProfile();
        studentProfile.setUserId(1);
        studentProfile.setUser(user);
        studentProfile.setFirstName("Test");
        studentProfile.setLastName("User");

        teacherProfile = new TeacherProfile();
        teacherProfile.setUserId(1);
        teacherProfile.setUser(user);
        teacherProfile.setFullName("Test Teacher");

        adminProfile = new AdministratorProfile();
        adminProfile.setUserId(1);
        adminProfile.setUser(user);
        adminProfile.setFullName("Test Admin");
    }

    @Test
    void testGetStudentProfile() {
        when(studentProfileRepository.findById(1)).thenReturn(Optional.of(studentProfile));

        StudentProfileDto profileDto = profileService.getStudentProfile(1);

        assertNotNull(profileDto);
        assertEquals("Test", profileDto.getFirstName());
    }

    @Test
    void testUpdateStudentProfile() {
        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("User");
        updateDto.setDateOfBirth(LocalDate.now());

        when(studentProfileRepository.findById(1)).thenReturn(Optional.of(studentProfile));
        when(studentProfileRepository.save(any(StudentProfile.class))).thenAnswer(i -> i.getArguments()[0]);

        StudentProfileDto updatedProfile = profileService.updateStudentProfile(1, updateDto);

        assertEquals("Updated", updatedProfile.getFirstName());
    }

    @Test
    void testUpdateTeacherProfile() {
        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("Teacher");

        when(teacherProfileRepository.findById(1)).thenReturn(Optional.of(teacherProfile));

        profileService.updateTeacherProfile(1, updateDto);

        assertEquals("Updated Teacher", teacherProfile.getFullName());
    }

    @Test
    void testUpdateAdminProfile() {
        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        updateDto.setFirstName("Updated");
        updateDto.setLastName("Admin");

        when(administratorProfileRepository.findById(1)).thenReturn(Optional.of(adminProfile));

        profileService.updateAdminProfile(1, updateDto);

        assertEquals("Updated Admin", adminProfile.getFullName());
    }
}