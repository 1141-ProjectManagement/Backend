package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.GradeUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/{courseId}/students")
    @PreAuthorize("hasAuthority('grade.view.all')")
    public ResponseEntity<List<StudentProfileDto>> getEnrolledStudents(@PathVariable Integer courseId) {
        List<StudentProfileDto> students = teacherService.getEnrolledStudents(courseId);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{courseId}/grades")
    @PreAuthorize("hasAuthority('grade.update')")
    public ResponseEntity<Void> updateGrades(@PathVariable Integer courseId, @RequestBody List<GradeUpdateDto> gradeUpdates) {
        teacherService.updateGrades(courseId, gradeUpdates);
        return ResponseEntity.ok().build();
    }
}
