package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.BadRequestException;
import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.repository.CourseRepository;
import edu.fcu.cs1133.repository.EnrollmentRepository;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class CourseService {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  /**
   * 處理學生選課的核心業務邏輯
   * @param studentId 執行選課的學生 User ID
   * @param courseId  要選修的課程 ID
   */
  @Transactional
  public void enrollStudentInCourse(Integer studentId, Integer courseId) {
    // 業務規則 1: 驗證學生是否存在
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));

    // 業務規則 2: 驗證該使用者是否為學生角色
    // 這邊的檢查應該基於權限，但為求簡化暫時保留
    if (!student.getRole().getRoleName().equals("Student")) { // Role name from DB is "Student"
      throw new BadRequestException("User is not a student and cannot enroll in courses.");
    }

    // 業務規則 3: 驗證課程是否存在
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

    // 業務規則 4: 檢查學生是否已經選修過此課程
    if (enrollmentRepository.existsByStudentUserIdAndCourseCourseId(studentId, courseId)) {
      throw new BadRequestException("Student is already enrolled in this course.");
    }

    // --- 所有驗證通過，執行操作 ---
    Enrollment enrollment = new Enrollment();
    enrollment.setStudent(student);
    enrollment.setCourse(course);
    enrollment.setEnrollmentDate(LocalDate.now());

    enrollmentRepository.save(enrollment);
  }

  /**
   * 獲取所有課程列表
   * @return 課程列表
   */
  @Transactional(readOnly = true)
  public List<Course> getAllCourses() {
    return courseRepository.findAll();
  }

  /**
   * 根據 ID 獲取單一課程
   * @param courseId 課程 ID
   * @return 課程
   */
  @Transactional(readOnly = true)
  public Course getCourseById(Integer courseId) {
    return courseRepository.findById(courseId)
        .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));
  }

  // TODO: 增加 createCourse(CourseCreationDto dto), updateCourse(Integer id, CourseUpdateDto dto) 和 deleteCourse(Integer id) 方法
}