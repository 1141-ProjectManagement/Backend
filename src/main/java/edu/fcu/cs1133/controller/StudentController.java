package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.response.EnrollmentDto;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

  @Autowired
  private StudentService studentService;

  @GetMapping("/me/enrollments")
  @PreAuthorize("hasRole('STUDENT')") // <-- 端點安全：只有 STUDENT 角色可以訪問
  public ResponseEntity<List<EnrollmentDto>> getMyEnrollments(
      @AuthenticationPrincipal UserPrincipal currentUser) { // <-- 注入當前登入使用者

    List<EnrollmentDto> enrollments = studentService.getMyEnrollments(currentUser);
    return ResponseEntity.ok(enrollments);
  }
}