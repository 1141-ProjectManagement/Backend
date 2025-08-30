package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.EnrollmentDto;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.ProfileService;
import edu.fcu.cs1133.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfileService profileService;

    @GetMapping("/me/enrollments")
    @PreAuthorize("hasAuthority('grade.view.own')")
    public ResponseEntity<List<EnrollmentDto>> getMyEnrollments(
            @AuthenticationPrincipal UserPrincipal currentUser) {

        List<EnrollmentDto> enrollments = studentService.getMyEnrollments(currentUser);
        return ResponseEntity.ok(enrollments);
    }

    @GetMapping("/me/profile")
    @PreAuthorize("hasAuthority('profile.view.own')")
    public ResponseEntity<StudentProfileDto> getMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        StudentProfileDto profile = profileService.getStudentProfile(currentUser.getId());
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    @PreAuthorize("hasAuthority('profile.edit.own')")
    public ResponseEntity<StudentProfileDto> updateMyProfile(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestBody ProfileUpdateDto profileUpdateDto) {
        StudentProfileDto updatedProfile = profileService.updateStudentProfile(currentUser.getId(), profileUpdateDto);
        return ResponseEntity.ok(updatedProfile);
    }
}
