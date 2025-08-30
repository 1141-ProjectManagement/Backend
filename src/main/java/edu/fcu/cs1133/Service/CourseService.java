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
  @Transactional // 關鍵！此註解確保整個方法在一個事務中執行。
  // 如果中途發生任何 RuntimeException，所有資料庫操作都會自動回滾。
  public void enrollStudentInCourse(Integer studentId, Integer courseId) {
    // 業務規則 1: 驗證學生是否存在
    User student = userRepository.findById(studentId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "id", studentId));

    // 業務規則 2: 驗證該使用者是否為學生角色
    if (!"STUDENT".equals(student.getRole().getRoleName())) {
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

    // 未來的擴充：可以在這裡更新學生的已修學分、檢查課程是否滿員等更複雜的邏輯。
  }

  // 可以在此添加其他課程相關的服務，例如：
  // public List<Course> getAllAvailableCourses() { ... }
  // public Course createCourse(CourseCreationRequest request) { ... }
}