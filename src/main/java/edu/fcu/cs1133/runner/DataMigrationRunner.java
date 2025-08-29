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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataMigrationRunner implements CommandLineRunner {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private TeachersRepository teachersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String DEFAULT_PASSWORD = "password123";

    @Override
    public void run(String... args) throws Exception {
        migrateUsers();
    }

    private void migrateUsers() {
        System.out.println("Checking for users needing data migration...");

        // 1. Get all existing usernames to check for duplicates
        Set<String> existingUsernames = new HashSet<>();
        studentsRepository.findAll().stream().map(Student::getUsername).forEach(existingUsernames::add);
        teachersRepository.findAll().stream().map(Teacher::getUsername).forEach(existingUsernames::add);

        // 2. Migrate Teachers
        List<Teacher> teachersToMigrate = teachersRepository.findAll().stream()
                .filter(t -> t.getUsername() == null || t.getUsername().isEmpty())
                .collect(Collectors.toList());

        if (!teachersToMigrate.isEmpty()) {
            System.out.println("Migrating " + teachersToMigrate.size() + " teacher records...");
            for (Teacher teacher : teachersToMigrate) {
                if (teacher.getEmail() != null && !teacher.getEmail().isEmpty()) {
                    String username = generateUniqueUsername(teacher.getEmail(), existingUsernames);
                    teacher.setUsername(username);
                    teacher.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
                    // Default new teachers to TEACHER role. Admins can be assigned manually.
                    if (teacher.getRole() == null) {
                        teacher.setRole(Role.TEACHER);
                    }
                    teachersRepository.save(teacher);
                    existingUsernames.add(username); // Add new username to set
                }
            }
            System.out.println("Teacher migration complete.");
        }

        // 3. Migrate Students
        List<Student> studentsToMigrate = studentsRepository.findAll().stream()
                .filter(s -> s.getUsername() == null || s.getUsername().isEmpty())
                .collect(Collectors.toList());

        if (!studentsToMigrate.isEmpty()) {
            System.out.println("Migrating " + studentsToMigrate.size() + " student records...");
            for (Student student : studentsToMigrate) {
                if (student.getEmail() != null && !student.getEmail().isEmpty()) {
                    String username = generateUniqueUsername(student.getEmail(), existingUsernames);
                    student.setUsername(username);
                    student.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
                    if (student.getRole() == null) {
                        student.setRole(Role.STUDENT);
                    }
                    studentsRepository.save(student);
                    existingUsernames.add(username); // Add new username to set
                }
            }
            System.out.println("Student migration complete.");
        }

        System.out.println("Data migration check finished.");
    }

    private String generateUniqueUsername(String email, Set<String> existingUsernames) {
        String baseUsername = email.split("@")[0].toLowerCase().replaceAll("[^a-z0-9]", "");
        if (baseUsername.isEmpty()) {
            baseUsername = "user";
        }

        String username = baseUsername;
        int counter = 1;
        while (existingUsernames.contains(username)) {
            username = baseUsername + counter;
            counter++;
        }
        return username;
    }
}
