package edu.fcu.cs1133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fcu.cs1133.config.SecurityConfig;
import edu.fcu.cs1133.payload.EnrollmentDto;
import edu.fcu.cs1133.payload.ProfileUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.security.CustomUserDetailsService;
import edu.fcu.cs1133.security.JwtTokenProvider;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.ProfileService;
import edu.fcu.cs1133.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "grade.view.own")
    public void testGetMyEnrollments() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("grade.view.own")));
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);
        when(studentService.getMyEnrollments(any(UserPrincipal.class))).thenReturn(Collections.singletonList(new EnrollmentDto()));

        mockMvc.perform(get("/api/users/me/enrollments").with(user(userPrincipal)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "profile.view.own")
    public void testGetMyProfile() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("profile.view.own")));
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);
        when(profileService.getStudentProfile(1)).thenReturn(new StudentProfileDto());

        mockMvc.perform(get("/api/users/me/profile").with(user(userPrincipal)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "profile.edit.own")
    public void testUpdateMyProfile() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("profile.edit.own")));
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);
        when(profileService.updateStudentProfile(any(), any(ProfileUpdateDto.class))).thenReturn(new StudentProfileDto());

        mockMvc.perform(put("/api/users/me/profile").with(csrf()).with(user(userPrincipal))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new ProfileUpdateDto())))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "wrong.authority")
    public void testGetMyProfile_Unauthorized() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("wrong.authority")));
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);

        mockMvc.perform(get("/api/users/me/profile").with(user(userPrincipal)))
                .andExpect(status().isForbidden());
    }
}
