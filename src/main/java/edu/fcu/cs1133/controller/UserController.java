package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.EnrollmentDto;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.ProfileService;
import edu.fcu.cs1133.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/me/enrollments")
    public ResponseEntity<List<EnrollmentDto>> getMyEnrollments(
            @AuthenticationPrincipal UserPrincipal currentUser) {

        List<EnrollmentDto> enrollments = studentService.getMyEnrollments(currentUser);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            return ResponseEntity.status(401).body(new edu.fcu.cs1133.payload.MessageResponse("User not authenticated"));
        }
        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();

        User user = profileService.getUserRepository().findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        String role = user.getRole() != null ? user.getRole().getRoleName() : "unknown";
        try {
            switch (role.toLowerCase()) {
                case "student":
                    return ResponseEntity.ok(profileService.getStudentProfile(user.getUserId()));
                case "teacher":
                    return ResponseEntity.ok(profileService.getTeacherProfile(user.getUserId()));
                case "admin":
                case "administrator":
                    return ResponseEntity.ok(profileService.getAdminProfile(user.getUserId()));
                default:
                    return ResponseEntity.status(404).body(new edu.fcu.cs1133.payload.MessageResponse("Unknown role: " + role));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(new edu.fcu.cs1133.payload.MessageResponse("Profile not found for userId=" + user.getUserId()));
        }
    }

    @PutMapping("/me/profile")
    public ResponseEntity<StudentProfileDto> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody ProfileUpdateDto profileUpdateDto) {
        StudentProfileDto updatedProfile = profileService.updateStudentProfile(currentUser.getId(), profileUpdateDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
