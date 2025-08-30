package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.CourseDto;
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
  public ResponseEntity<List<CourseDto>> getAllCourses() {
      List<CourseDto> courses = courseService.getAllCourses();
      return ResponseEntity.ok(courses);
  }

  @GetMapping("/{courseId}")
  @PreAuthorize("hasAuthority('course.view.catalog')")
  public ResponseEntity<CourseDto> getCourseById(@PathVariable Integer courseId) {
      CourseDto course = courseService.getCourseById(courseId);
      return ResponseEntity.ok(course);
  }

  @PostMapping("/{courseId}/enroll")
  @PreAuthorize("hasAuthority('course.enroll.self')")
  public ResponseEntity<?> enrollInCourse(
      @AuthenticationPrincipal UserPrincipal currentUser,
      @PathVariable Integer courseId) {

    courseService.enrollStudentInCourse(currentUser.getId(), courseId);

    return ResponseEntity.ok(new MessageResponse("Enrolled successfully in course " + courseId));
  }

  // TODO: Add POST, PUT, DELETE endpoints for course management
}