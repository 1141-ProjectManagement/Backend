package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.StudentProfile;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.repository.StudentProfileRepository;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    // Add other profile repositories...

    @Transactional(readOnly = true)
    public StudentProfile getStudentProfile(Integer userId) {
        // First check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return studentProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("StudentProfile", "userId", userId));
    }

    // TODO: Add methods to get other profiles (Teacher, Admin)
    // TODO: Add methods to update profiles, which will take DTOs as input
}
