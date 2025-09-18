package edu.fcu.cs1133.runner;

import edu.fcu.cs1133.model.Role;
import edu.fcu.cs1133.model.Student;
import edu.fcu.cs1133.model.Teacher;
import edu.fcu.cs1133.repository.StudentsRepository;
import edu.fcu.cs1133.repository.TeachersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// @Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        if (teachersRepository.count() == 0 && studentsRepository.count() == 0) {
            System.out.println("No users found. Seeding initial data...");

            // Create Admin User (as a Teacher)
            Teacher admin = new Teacher();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setName("Admin User");
            admin.setEmail("admin@test.com");
            admin.setAge(40);
            admin.setRole(Role.ADMIN);
            teachersRepository.save(admin);

            // Create Teacher User
            Teacher teacher = new Teacher();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("password123"));
            teacher.setName("Teacher User");
            teacher.setEmail("teacher@test.com");
            teacher.setAge(35);
            teacher.setRole(Role.TEACHER);
            teachersRepository.save(teacher);

            // Create Student User
            Student student = new Student();
            student.setUsername("student");
            student.setPassword(passwordEncoder.encode("password123"));
            student.setFirstName("Student");
            student.setLastName("User");
            student.setEmail("student@test.com");
            student.setDateOfBirth(LocalDate.of(2002, 5, 20));
            student.setRole(Role.STUDENT);
            studentsRepository.save(student);
            
            System.out.println("Initial users seeded successfully.");
        }
    }
}
