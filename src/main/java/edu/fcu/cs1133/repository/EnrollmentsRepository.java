package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentsRepository extends JpaRepository<Enrollment, EnrollmentId> {
    List<Enrollment> findByCourseCourseId(int courseId);
    List<Enrollment> findByStudentStudentId(int studentId);
}
