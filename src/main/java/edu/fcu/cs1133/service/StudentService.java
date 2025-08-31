package edu.fcu.cs1133.service;

import edu.fcu.cs1133.payload.EnrollmentDto;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.repository.EnrollmentRepository;
import edu.fcu.cs1133.repository.UserRepository;
import edu.fcu.cs1133.security.UserPrincipal; // Spring Security 的 UserDetails 實作
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

  @Autowired
  private EnrollmentRepository enrollmentRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   * 獲取當前登入學生的所有選課紀錄（包括成績）
   * @param currentUserPrincipal 當前登入使用者的身分主體，由 Controller 傳入
   * @return DTO 列表，避免直接暴露 Entity
   */
  @Transactional(readOnly = true) // 查詢操作，標示為唯讀事務可以優化性能
  public List<EnrollmentDto> getMyEnrollments(UserPrincipal currentUserPrincipal) {
    // 從安全上下文中獲取當前使用者的 official_id
    String officialId = currentUserPrincipal.getUsername(); // 注意：UserDetails 的 getUsername() 返回的是我們的 official_id

    // 根據 official_id 找到對應的 User Entity
    User currentUser = userRepository.findByOfficialId(officialId)
        .orElseThrow(() -> new ResourceNotFoundException("User", "official_id", officialId));

    // 根據 user_id 查詢該學生的所有選課紀錄
    List<Enrollment> enrollments = enrollmentRepository.findByStudentUserId(currentUser.getUserId());

    // 將 Entity 列表轉換為 DTO 列表
    return enrollments.stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  private EnrollmentDto convertToDto(Enrollment enrollment) {
    // 這是一個簡單的轉換邏輯，將 Entity 的數據映射到 DTO
    EnrollmentDto dto = new EnrollmentDto();
    dto.setCourseName(enrollment.getCourse().getCourseName());
    dto.setCredits(enrollment.getCourse().getCredits());
    dto.setEnrollmentDate(enrollment.getEnrollmentDate());
    dto.setGrade(enrollment.getGrade());
    // ... 根據需要設置更多欄位
    return dto;
  }
}