package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.BadRequestException;
import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.model.Role;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.CourseCreationDto;
import edu.fcu.cs1133.payload.CourseDto;
import edu.fcu.cs1133.repository.CourseRepository;
import edu.fcu.cs1133.repository.EnrollmentRepository;
import edu.fcu.cs1133.repository.TeacherProfileRepository;
import edu.fcu.cs1133.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private TeacherProfileRepository teacherProfileRepository;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private User student;
    private Role studentRole;

    @BeforeEach
    void setUp() {
        course = new Course();
        course.setCourseId(1);
        course.setCourseName("Test Course");
        course.setCourseDescription("Test Description");
        course.setCredits(3);

        studentRole = new Role();
        studentRole.setRoleId(1);
        studentRole.setRoleName("Student");

        student = new User();
        student.setUserId(1);
        student.setOfficialId("student123");
        student.setRole(studentRole);
    }

    @Test
    void testGetAllCourses() {
        when(courseRepository.findAll()).thenReturn(Collections.singletonList(course));

        List<CourseDto> courseDtos = courseService.getAllCourses();

        assertEquals(1, courseDtos.size());
        assertEquals("Test Course", courseDtos.get(0).getCourseName());
    }

    @Test
    void testGetCourseById() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        CourseDto courseDto = courseService.getCourseById(1);

        assertNotNull(courseDto);
        assertEquals("Test Course", courseDto.getCourseName());
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.getCourseById(1);
        });
    }

    @Test
    void testCreateCourse() {
        CourseCreationDto creationDto = new CourseCreationDto();
        creationDto.setCourseName("New Course");

        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);

        CourseDto createdCourse = courseService.createCourse(creationDto);

        assertEquals("New Course", createdCourse.getCourseName());
    }

    @Test
    void testUpdateCourse() {
        CourseCreationDto updateDto = new CourseCreationDto();
        updateDto.setCourseName("Updated Course");

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);

        CourseDto updatedCourse = courseService.updateCourse(1, updateDto);

        assertEquals("Updated Course", updatedCourse.getCourseName());
    }

    @Test
    void testDeleteCourse() {
        when(courseRepository.existsById(1)).thenReturn(true);
        doNothing().when(courseRepository).deleteById(1);

        courseService.deleteCourse(1);

        verify(courseRepository, times(1)).deleteById(1);
    }

    @Test
    void testEnrollStudentInCourse() {
        when(userRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentUserIdAndCourseCourseId(1, 1)).thenReturn(false);

        courseService.enrollStudentInCourse(1, 1);

        verify(enrollmentRepository, times(1)).save(any());
    }

    @Test
    void testEnrollStudentInCourse_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.enrollStudentInCourse(1, 1);
        });
    }

    @Test
    void testEnrollStudentInCourse_NotAStudent() {
        Role teacherRole = new Role();
        teacherRole.setRoleName("Teacher");
        student.setRole(teacherRole);
        when(userRepository.findById(1)).thenReturn(Optional.of(student));

        assertThrows(BadRequestException.class, () -> {
            courseService.enrollStudentInCourse(1, 1);
        });
    }

    @Test
    void testEnrollStudentInCourse_CourseNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courseService.enrollStudentInCourse(1, 1);
        });
    }

    @Test
    void testEnrollStudentInCourse_AlreadyEnrolled() {
        when(userRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentUserIdAndCourseCourseId(1, 1)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            courseService.enrollStudentInCourse(1, 1);
        });
    }
}