package edu.fcu.cs1133.repository;

import edu.fcu.cs1133.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeachersRepository extends JpaRepository<Teacher, Integer> {
    // JpaRepository already provides methods for CRUD operations
    // You can define custom query methods here if needed
    List<Teacher> findByName(String name);
    List<Teacher> findByNameContainingIgnoreCase(String name);

    List<Teacher> findByEmail(String email);
    List<Teacher> findByEmailContainingIgnoreCase(String email);
    List<Teacher> findByAge(int age);
    List<Teacher> findByAgeGreaterThanEqual(int age);
    List<Teacher> findByAgeLessThanEqual(int age);

    Optional<Teacher> findByUsername(String username);
}
