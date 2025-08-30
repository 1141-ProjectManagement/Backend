package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.EnrollmentId; // 假設複合主鍵類
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

  // 根據學生 ID 查詢所有選課紀錄
  List<Enrollment> findByStudentUserId(Integer userId);

  // 檢查某個學生是否已經選了某門課
  boolean existsByStudentUserIdAndCourseCourseId(Integer userId, Integer courseId);

}

