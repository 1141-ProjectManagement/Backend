package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.payload.MessageResponse;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

  @Autowired
  private CourseService courseService;

  @GetMapping
  @PreAuthorize("hasAuthority('course.view.catalog')")
  public ResponseEntity<List<Course>> getAllCourses() {
      List<Course> courses = courseService.getAllCourses();
      return ResponseEntity.ok(courses);
  }

  @GetMapping("/{courseId}")
  @PreAuthorize("hasAuthority('course.view.catalog')")
  public ResponseEntity<Course> getCourseById(@PathVariable Integer courseId) {
      Course course = courseService.getCourseById(courseId);
      return ResponseEntity.ok(course);
  }

  @PostMapping("/{courseId}/enroll")
  @PreAuthorize("hasAuthority('course.enroll.self')")
  public ResponseEntity<?> enrollInCourse(
      @AuthenticationPrincipal UserPrincipal currentUser,
      @PathVariable Integer courseId) {

    // 從 Principal 中獲取 user_id，確保學生只能為自己選課
    courseService.enrollStudentInCourse(currentUser.getId(), courseId);

    return ResponseEntity.ok(new MessageResponse("Enrolled successfully in course " + courseId));
  }

  // TODO: Add POST, PUT, DELETE endpoints for course management
}