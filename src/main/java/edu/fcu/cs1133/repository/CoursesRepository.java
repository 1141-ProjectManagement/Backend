package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoursesRepository extends JpaRepository<Course, Integer> {
    List<Course> findByTeacherTeacherId(int teacherId);
}
