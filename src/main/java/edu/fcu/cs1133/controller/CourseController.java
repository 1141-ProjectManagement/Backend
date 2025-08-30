package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.response.MessageResponse;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired
  private CourseService courseService;

  // 可以在這裡添加一個 @GetMapping 來獲取所有課程列表

  @PostMapping("/{courseId}/enroll")
  @PreAuthorize("hasRole('STUDENT')")
  public ResponseEntity<?> enrollInCourse(
      @AuthenticationPrincipal UserPrincipal currentUser,
      @PathVariable Integer courseId) {

    // 從 Principal 中獲取 user_id，確保學生只能為自己選課
    courseService.enrollStudentInCourse(currentUser.getId(), courseId);

    return ResponseEntity.ok(new MessageResponse("Enrolled successfully in course " + courseId));
  }
}