package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.model.Teacher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import edu.fcu.cs1133.repository.TeachersRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TeacherService {

  @Autowired TeachersRepository teachersRepository;

  public List<Teacher> getAllTeachers() {
    return teachersRepository.findAll();
  }

  public Optional<Teacher> getTeacherById(int id) {
    return teachersRepository.findById(id);
  }

  public Teacher createTeacher(Teacher teacher) {
    return teachersRepository.save(teacher);
  }

  public Teacher updateTeacher(int id, Teacher teacher) {
    Optional<Teacher> existingTeacher = teachersRepository.findById(id);
    if (existingTeacher.isPresent()) {
      Teacher updatedTeacher = existingTeacher.get();
      updatedTeacher.setName(teacher.getName());
      updatedTeacher.setEmail(teacher.getEmail());
      updatedTeacher.setAge(teacher.getAge());
      return teachersRepository.save(updatedTeacher);
    }
    return null; // or throw an exception
  }

  public void deleteTeacher(int id) {
    teachersRepository.deleteById(id);
  }

  public List<Teacher> findByName(String name) {
    return teachersRepository.findByNameContainingIgnoreCase(name);
  }

  public List<Teacher> findByEmail(String email) {
    return teachersRepository.findByEmailContainingIgnoreCase(email);
  }

  public List<Teacher> findByAge(int age) {
    return teachersRepository.findByAge(age);
  }
}
