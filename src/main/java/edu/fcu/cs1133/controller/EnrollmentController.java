package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.Service.EnrollmentService;
import edu.fcu.cs1133.model.Enrollment;
import edu.fcu.cs1133.model.EnrollmentId;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollment API", description = "Operations related to enrollments")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    // Getting a single enrollment requires the composite key
    @GetMapping("/get")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Enrollment> getEnrollmentById(@RequestBody EnrollmentId id) {
        return enrollmentService.getEnrollmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
        Enrollment result = enrollmentService.createEnrollment(enrollment);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    // Updating requires the composite key in the body to identify the record
    @PutMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TEACHER')")
    public ResponseEntity<Enrollment> updateEnrollment(@RequestBody Enrollment enrollmentDetails) {
        Enrollment updatedEnrollment = enrollmentService.updateEnrollment(enrollmentDetails.getId(), enrollmentDetails);
        if (updatedEnrollment != null) {
            return ResponseEntity.ok(updatedEnrollment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deleting also requires the composite key
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    public ResponseEntity<Void> deleteEnrollment(@RequestBody EnrollmentId id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAuthority('STUDENT')")
    public List<Enrollment> getEnrollmentsByStudentId(@PathVariable int studentId) {
        return enrollmentService.getEnrollmentsByStudentId(studentId);
    }
}