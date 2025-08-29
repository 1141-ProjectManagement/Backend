package edu.fcu.cs1133.Service;

import edu.fcu.cs1133.model.Student;
import edu.fcu.cs1133.repository.StudentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentsRepository studentsRepository;

    public List<Student> getAllStudents() {
        return studentsRepository.findAll();
    }

    public Optional<Student> getStudentById(int id) {
        return studentsRepository.findById(id);
    }

    public Student createStudent(Student student) {
        return studentsRepository.save(student);
    }

    public Student updateStudent(int id, Student studentDetails) {
        Optional<Student> optionalStudent = studentsRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setEmail(studentDetails.getEmail());
            existingStudent.setDateOfBirth(studentDetails.getDateOfBirth());
            return studentsRepository.save(existingStudent);
        }
        return null; // Or throw an exception
    }

    public void deleteStudent(int id) {
        studentsRepository.deleteById(id);
    }

    public List<Student> getStudentsByFirstName(String firstName) {
        return studentsRepository.findByFirstName(firstName);
    }

    public List<Student> getStudentsByLastName(String lastName) {
        return studentsRepository.findByLastName(lastName);
    }

    public List<Student> getStudentsByLastNameAndFirstName(String lastName, String firstName) {
        return studentsRepository.findByLastNameAndFirstName(lastName, firstName);
    }
}
