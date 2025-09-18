package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.EnrollmentId;
import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.model.Student;
import edu.fcu.cs1133.repository.EnrollmentsRepository;
import edu.fcu.cs1133.repository.CoursesRepository;
import edu.fcu.cs1133.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentsRepository enrollmentsRepository;
    @Autowired
    private StudentsRepository studentsRepository;
    @Autowired
    private CoursesRepository coursesRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentsRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(EnrollmentId id) {
        return enrollmentsRepository.findById(id);
    }

    public Enrollment createEnrollment(Enrollment enrollment) {
        try {
            // 防呆：id 必須存在
            if (enrollment.getId() == null) {
                return null;
            }
            // 補全 student 關聯
            int studentId = enrollment.getId().getStudentId();
            int courseId = enrollment.getId().getCourseId();
            Student student = studentsRepository.findById(studentId).orElse(null);
            Course course = coursesRepository.findById(courseId).orElse(null);
            if (student == null || course == null) {
                // 資料不存在，回傳 null 或自訂錯誤
                return null;
            }
            enrollment.setStudent(student);
            enrollment.setCourse(course);
            // 檢查是否重複選課
            if (enrollmentsRepository.existsById(enrollment.getId())) {
                // 已選過，回傳 null 或自訂錯誤
                return null;
            }
            return enrollmentsRepository.save(enrollment);
        } catch (Exception e) {
            // 捕捉所有例外，避免 500
            e.printStackTrace();
            return null;
        }
    }

    public Enrollment updateEnrollment(EnrollmentId id, Enrollment enrollmentDetails) {
        Optional<Enrollment> optionalEnrollment = enrollmentsRepository.findById(id);
        if (optionalEnrollment.isPresent()) {
            Enrollment existingEnrollment = optionalEnrollment.get();
            // Only grade and enrollmentDate are likely to be updated
            existingEnrollment.setGrade(enrollmentDetails.getGrade());
            existingEnrollment.setEnrollmentDate(enrollmentDetails.getEnrollmentDate());
            return enrollmentsRepository.save(existingEnrollment);
        }
        return null; // Or throw an exception
    }

    public void deleteEnrollment(EnrollmentId id) {
        enrollmentsRepository.deleteById(id);
    }

    public List<Enrollment> getEnrollmentsByCourseId(int courseId) {
        return enrollmentsRepository.findByCourseCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        return enrollmentsRepository.findByStudentStudentId(studentId);
    }
}
