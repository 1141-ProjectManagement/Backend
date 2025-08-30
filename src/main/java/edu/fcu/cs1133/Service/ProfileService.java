package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.StudentProfile;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.repository.StudentProfileRepository;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private edu.fcu.cs1133.repository.TeacherProfileRepository teacherProfileRepository;

    @Autowired
    private edu.fcu.cs1133.repository.AdministratorProfileRepository administratorProfileRepository;

    // Add other profile repositories...

    @Transactional(readOnly = true)
    public StudentProfileDto getStudentProfile(Integer userId) {
        StudentProfile profile = studentProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentProfile", "userId", userId));
        return convertToDto(profile);
    }

    public StudentProfileDto convertToDto(StudentProfile profile) {
        StudentProfileDto dto = new StudentProfileDto();
        User user = profile.getUser(); // This is safe within the transaction

        dto.setUserId(profile.getUserId());
        dto.setFirstName(profile.getFirstName());
        dto.setLastName(profile.getLastName());
        dto.setDateOfBirth(profile.getDateOfBirth());

        if (user != null) {
            dto.setOfficialId(user.getOfficialId());
            dto.setEmail(user.getEmail());
        }
        return dto;
    }

    // TODO: Add methods to get other profiles (Teacher, Admin)

    @Transactional
    public StudentProfileDto updateStudentProfile(Integer userId, edu.fcu.cs1133.payload.ProfileUpdateDto profileUpdateDto) {
        StudentProfile profile = studentProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("StudentProfile", "userId", userId));

        profile.setFirstName(profileUpdateDto.getFirstName());
        profile.setLastName(profileUpdateDto.getLastName());
        profile.setDateOfBirth(profileUpdateDto.getDateOfBirth());

        StudentProfile updatedProfile = studentProfileRepository.save(profile);
        return convertToDto(updatedProfile);
    }

    @Transactional
    public void updateTeacherProfile(Integer userId, edu.fcu.cs1133.payload.ProfileUpdateDto profileUpdateDto) {
        edu.fcu.cs1133.model.TeacherProfile profile = teacherProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("TeacherProfile", "userId", userId));

        // Assuming fullName is constructed from firstName and lastName
        String fullName = (profileUpdateDto.getFirstName() + " " + profileUpdateDto.getLastName()).trim();
        if (fullName.isEmpty()) {
            fullName = profileUpdateDto.getFullName();
        }
        profile.setFullName(fullName);
        profile.setAge(profileUpdateDto.getAge());

        teacherProfileRepository.save(profile);
    }

    @Transactional
    public void updateAdminProfile(Integer userId, edu.fcu.cs1133.payload.ProfileUpdateDto profileUpdateDto) {
        edu.fcu.cs1133.model.AdministratorProfile profile = administratorProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("AdministratorProfile", "userId", userId));

        // Assuming fullName is constructed from firstName and lastName
        String fullName = (profileUpdateDto.getFirstName() + " " + profileUpdateDto.getLastName()).trim();
        if (fullName.isEmpty()) {
            fullName = profileUpdateDto.getFullName();
        }
        profile.setFullName(fullName);

        administratorProfileRepository.save(profile);
    }
}
