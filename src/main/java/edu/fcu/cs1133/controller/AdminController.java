package edu.fcu.cs1133.controller;

import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.UserCreationDto;
import edu.fcu.cs1133.payload.UserDto;
import edu.fcu.cs1133.service.SystemSettingService;
import edu.fcu.cs1133.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemSettingService systemSettingService;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('student.view') or hasAuthority('teacher.view')") // Example permission
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('student.view') or hasAuthority('teacher.view')") // Example permission
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/students")
    @PreAuthorize("hasAuthority('student.create')")
    public ResponseEntity<UserDto> createStudent(@RequestBody UserCreationDto userDto) {
        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/students/{userId}")
    @PreAuthorize("hasAuthority('student.update')")
    public ResponseEntity<UserDto> updateStudent(@PathVariable Integer userId, @RequestBody ProfileUpdateDto profileDto) {
        UserDto updatedUser = userService.updateStudentProfile(userId, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/students/{userId}")
    @PreAuthorize("hasAuthority('student.delete')")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/teachers")
    @PreAuthorize("hasAuthority('teacher.view')")
    public ResponseEntity<List<UserDto>> getAllTeachers() {
        List<UserDto> users = userService.getAllTeachers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/teachers")
    @PreAuthorize("hasAuthority('teacher.create')")
    public ResponseEntity<UserDto> createTeacher(@RequestBody UserCreationDto userDto) {
        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/teachers/{userId}")
    @PreAuthorize("hasAuthority('teacher.update')")
    public ResponseEntity<UserDto> updateTeacher(@PathVariable Integer userId, @RequestBody ProfileUpdateDto profileDto) {
        UserDto updatedUser = userService.updateTeacherProfile(userId, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/teachers/{userId}")
    @PreAuthorize("hasAuthority('teacher.delete')")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAuthority('admin.view')")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        List<UserDto> users = userService.getAllAdmins();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/admins")
    @PreAuthorize("hasAuthority('admin.create')")
    public ResponseEntity<UserDto> createAdmin(@RequestBody UserCreationDto userDto) {
        UserDto newUser = userService.createUser(userDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/admins/{userId}")
    @PreAuthorize("hasAuthority('admin.update')")
    public ResponseEntity<UserDto> updateAdmin(@PathVariable Integer userId, @RequestBody ProfileUpdateDto profileDto) {
        UserDto updatedUser = userService.updateAdminProfile(userId, profileDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/admins/{userId}")
    @PreAuthorize("hasAuthority('admin.delete')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/settings")
    @PreAuthorize("hasAuthority('system.settings.view')")
    public ResponseEntity<Map<String, String>> getSettings() {
        Map<String, String> settings = systemSettingService.getSettings();
        return ResponseEntity.ok(settings);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('system.settings.edit')")
    public ResponseEntity<Void> updateSettings(@RequestBody Map<String, String> settings) {
        systemSettingService.updateSettings(settings);
        return ResponseEntity.ok().build();
    }
}