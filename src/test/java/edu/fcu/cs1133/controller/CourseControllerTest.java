package edu.fcu.cs1133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fcu.cs1133.config.SecurityConfig;
import edu.fcu.cs1133.payload.CourseCreationDto;
import edu.fcu.cs1133.payload.CourseDto;
import edu.fcu.cs1133.security.CustomUserDetailsService;
import edu.fcu.cs1133.security.JwtTokenProvider;
import edu.fcu.cs1133.security.UserPrincipal;
import edu.fcu.cs1133.service.CourseService;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "course.view.catalog")
    public void testGetAllCourses() throws Exception {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("Test Course");
        when(courseService.getAllCourses()).thenReturn(Collections.singletonList(courseDto));

        mockMvc.perform(get("/api/courses").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].courseName").value("Test Course"));
    }

    @Test
    @WithMockUser(authorities = "course.enroll.self")
    public void testEnrollInCourse() throws Exception {
        UserPrincipal userPrincipal = new UserPrincipal(1, "testuser", "password", Collections.singletonList(new SimpleGrantedAuthority("course.enroll.self")));
        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(userPrincipal);
        doNothing().when(courseService).enrollStudentInCourse(1, 1);

        mockMvc.perform(post("/api/courses/1/enroll").with(csrf()).with(user(userPrincipal)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "course.create")
    public void testCreateCourse() throws Exception {
        CourseCreationDto creationDto = new CourseCreationDto();
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseName("New Course");

        when(courseService.createCourse(any(CourseCreationDto.class))).thenReturn(courseDto);

        mockMvc.perform(post("/api/courses").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseName").value("New Course"));
    }

    @Test
    @WithMockUser(authorities = "wrong.authority")
    public void testCreateCourse_Unauthorized() throws Exception {
        CourseCreationDto creationDto = new CourseCreationDto();

        mockMvc.perform(post("/api/courses").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(creationDto)))
                .andExpect(status().isForbidden());
    }
}