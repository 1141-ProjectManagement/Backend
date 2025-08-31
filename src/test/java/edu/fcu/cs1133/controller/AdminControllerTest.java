package edu.fcu.cs1133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fcu.cs1133.config.SecurityConfig;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.UserCreationDto;
import edu.fcu.cs1133.payload.UserDto;
import edu.fcu.cs1133.security.CustomUserDetailsService;
import edu.fcu.cs1133.security.JwtTokenProvider;
import edu.fcu.cs1133.service.SystemSettingService;
import edu.fcu.cs1133.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@Import(SecurityConfig.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SystemSettingService systemSettingService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    // --- Student Management Tests ---
    @Test
    @WithMockUser(authorities = "student.view")
    public void testGetAllStudents() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setRoleName("Student");
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    @WithMockUser(authorities = "student.create")
    public void testCreateStudent() throws Exception {
        UserCreationDto creationDto = new UserCreationDto();
        UserDto userDto = new UserDto();
        userDto.setOfficialId("newStudent");

        when(userService.createUser(any(UserCreationDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/admin/students").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.officialId").value("newStudent"));
    }

    @Test
    @WithMockUser(authorities = "student.update")
    public void testUpdateStudent() throws Exception {
        ProfileUpdateDto updateDto = new ProfileUpdateDto();
        UserDto userDto = new UserDto();
        userDto.setId(1);

        when(userService.updateStudentProfile(eq(1), any(ProfileUpdateDto.class))).thenReturn(userDto);

        mockMvc.perform(put("/api/admin/students/1").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "student.delete")
    public void testDeleteStudent() throws Exception {
        doNothing().when(userService).deleteUser(1);
        mockMvc.perform(delete("/api/admin/students/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    // --- Teacher Management Tests ---
    @Test
    @WithMockUser(authorities = "teacher.view")
    public void testGetAllTeachers() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setRoleName("Teacher");
        when(userService.getAllTeachers()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/teachers").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // --- Admin Management Tests ---
    @Test
    @WithMockUser(authorities = "admin.view")
    public void testGetAllAdmins() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setRoleName("Admin");
        when(userService.getAllAdmins()).thenReturn(Collections.singletonList(userDto));

        mockMvc.perform(get("/api/admin/admins").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    // --- System Settings Tests ---
    @Test
    @WithMockUser(authorities = "system.settings.view")
    public void testGetSettings() throws Exception {
        when(systemSettingService.getSettings()).thenReturn(Map.of("key", "value"));

        mockMvc.perform(get("/api/admin/settings").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("value"));
    }

    @Test
    @WithMockUser(authorities = "system.settings.edit")
    public void testUpdateSettings() throws Exception {
        Map<String, String> settings = Map.of("key", "newValue");
        doNothing().when(systemSettingService).updateSettings(settings);

        mockMvc.perform(put("/api/admin/settings").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isOk());
    }

    // --- Unauthorized Access Tests ---
    @Test
    @WithMockUser(authorities = "wrong.authority")
    public void testCreateStudent_Unauthorized() throws Exception {
        UserCreationDto creationDto = new UserCreationDto();

        mockMvc.perform(post("/api/admin/students").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isForbidden());
    }
}
