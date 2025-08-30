package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.BadRequestException;
import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.CourseCreationDto;
import edu.fcu.cs1133.payload.CourseDto;
import edu.fcu.cs1133.repository.CourseRepository;
import edu.fcu.cs1133.repository.EnrollmentRepository;
import edu.fcu.cs1133.repository.TeacherProfileRepository;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private TeacherProfileRepository teacherProfileRepository;

  @Transactional
  public void enrollStudentInCourse(Integer studentId, Integer courseId) {
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));

    if (!student.getRole().getRoleName().equals("Student")) {
      throw new BadRequestException("User is not a student and cannot enroll in courses.");
    }

    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

    if (enrollmentRepository.existsByStudentUserIdAndCourseCourseId(studentId, courseId)) {
      throw new BadRequestException("Student is already enrolled in this course.");
    }

    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setEnrollmentDate(LocalDate.now());

    enrollmentRepository.save(enrollment);
  }

  @Transactional(readOnly = true)
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public CourseDto getCourseById(Integer courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
    return convertToDto(course);
  }

  @Transactional
  public CourseDto createCourse(CourseCreationDto courseDto) {
      Course course = new Course();
      course.setCourseName(courseDto.getCourseName());
      course.setCourseDescription(courseDto.getCourseDescription());
      course.setCredits(courseDto.getCredits());

      if (courseDto.getTeacherId() != null) {
          edu.fcu.cs1133.model.TeacherProfile teacher = teacherProfileRepository.findById(courseDto.getTeacherId())
                  .orElseThrow(() -> new ResourceNotFoundException("TeacherProfile", "id", courseDto.getTeacherId()));
          course.setTeacher(teacher);
      }

      Course savedCourse = courseRepository.save(course);
      return convertToDto(savedCourse);
  }

  @Transactional
  public CourseDto updateCourse(Integer courseId, CourseCreationDto courseDto) {
      Course course = courseRepository.findById(courseId)
              .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

      course.setCourseName(courseDto.getCourseName());
      course.setCourseDescription(courseDto.getCourseDescription());
      course.setCredits(courseDto.getCredits());

      if (courseDto.getTeacherId() != null) {
          edu.fcu.cs1133.model.TeacherProfile teacher = teacherProfileRepository.findById(courseDto.getTeacherId())
                  .orElseThrow(() -> new ResourceNotFoundException("TeacherProfile", "id", courseDto.getTeacherId()));
          course.setTeacher(teacher);
      } else {
          course.setTeacher(null);
      }

      Course updatedCourse = courseRepository.save(course);
      return convertToDto(updatedCourse);
  }

  @Transactional
  public void deleteCourse(Integer courseId) {
      if (!courseRepository.existsById(courseId)) {
          throw new ResourceNotFoundException("Course", "id", courseId);
      }
      courseRepository.deleteById(courseId);
  }

  private CourseDto convertToDto(Course course) {
      CourseDto dto = new CourseDto();
      dto.setId(course.getCourseId());
      dto.setCourseName(course.getCourseName());
      dto.setCourseDescription(course.getCourseDescription());
      dto.setCredits(course.getCredits());
      if (course.getTeacher() != null && course.getTeacher().getFullName() != null) {
          dto.setTeacherName(course.getTeacher().getFullName());
      }
      return dto;
  }
}