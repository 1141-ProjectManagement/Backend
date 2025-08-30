package edu.fcu.cs1133.service;

import edu.fcu.cs1133.exception.ResourceNotFoundException;
import edu.fcu.cs1133.model.User;
import edu.fcu.cs1133.payload.UserDto;
import edu.fcu.cs1133.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return convertToDto(user);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setOfficialId(user.getOfficialId());
        userDto.setEmail(user.getEmail());
        userDto.setActive(user.getIsActive());
        if (user.getRole() != null) {
            userDto.setRoleName(user.getRole().getRoleName());
        }
        return userDto;
    }

    // TODO: Add methods for createUser, updateUser, deleteUser, assignRole etc.
}
