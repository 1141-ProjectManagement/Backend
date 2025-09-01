package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.Service.EnrollmentService;
import edu.fcu.cs1133.Service.StudentService;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.Student;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Student API", description = "Operations related to students")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER') or (hasAuthority('STUDENT') and #id == principal.getStudentId())")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/enrollments")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('STUDENT') and #id == principal.getStudentId())")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable int id) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(id);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('STUDENT') and #id == principal.getStudentId())")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(id, studentDetails);
        if (updatedStudent != null) {
            return ResponseEntity.ok(updatedStudent);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/firstname")
    public List<Student> getStudentsByFirstName(@RequestParam String firstName) {
        return studentService.getStudentsByFirstName(firstName);
    }

    @GetMapping("/search/lastname")
    public List<Student> getStudentsByLastName(@RequestParam String lastName) {
        return studentService.getStudentsByLastName(lastName);
    }

    @GetMapping("/search/fullname")
    public List<Student> getStudentsByLastNameAndFirstName(@RequestParam String lastName, @RequestParam String firstName) {
        return studentService.getStudentsByLastNameAndFirstName(lastName, firstName);
    }
}
