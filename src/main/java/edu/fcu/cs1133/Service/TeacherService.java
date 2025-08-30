package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.StudentProfile;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.repository.CourseRepository;
import edu.fcu.cs1133.repository.EnrollmentRepository;
import edu.fcu.cs1133.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private ProfileService profileService;

    @Transactional(readOnly = true)
    public List<StudentProfileDto> getEnrolledStudents(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseCourseId(courseId);
        return enrollments.stream()
                .map(enrollment -> studentProfileRepository.findById(enrollment.getStudent().getUserId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(profileService::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateGrades(Integer courseId, List<edu.fcu.cs1133.payload.GradeUpdateDto> gradeUpdates) {
        if (!courseRepository.existsById(courseId)) {
            throw new ResourceNotFoundException("Course", "id", courseId);
        }

        for (edu.fcu.cs1133.payload.GradeUpdateDto gradeUpdate : gradeUpdates) {
            Enrollment enrollment = enrollmentRepository.findByStudentUserIdAndCourseCourseId(gradeUpdate.getStudentId(), courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "studentId/courseId", gradeUpdate.getStudentId() + "/" + courseId));
            enrollment.setGrade(gradeUpdate.getGrade());
            enrollmentRepository.save(enrollment);
        }
    }
}
