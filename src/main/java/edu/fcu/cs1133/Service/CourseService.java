package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.model.Course;
import edu.fcu.cs1133.repository.CoursesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CoursesRepository coursesRepository;

    public List<Course> getAllCourses() {
        return coursesRepository.findAll();
    }

    public Optional<Course> getCourseById(int id) {
        return coursesRepository.findById(id);
    }

    public Course createCourse(Course course) {
        return coursesRepository.save(course);
    }

    public Course updateCourse(int id, Course courseDetails) {
        Optional<Course> optionalCourse = coursesRepository.findById(id);
        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setCourseName(courseDetails.getCourseName());
            existingCourse.setCourseDescription(courseDetails.getCourseDescription());
            existingCourse.setCredits(courseDetails.getCredits());
            existingCourse.setTeacher(courseDetails.getTeacher());
            return coursesRepository.save(existingCourse);
        }
        return null; // Or throw an exception
    }

    public void deleteCourse(int id) {
        coursesRepository.deleteById(id);
    }

    public List<Course> getCoursesByTeacherId(int teacherId) {
        return coursesRepository.findByTeacherTeacherId(teacherId);
    }
}
