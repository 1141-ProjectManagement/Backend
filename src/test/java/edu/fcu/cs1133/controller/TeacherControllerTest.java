package edu.fcu.cs1133.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.fcu.cs1133.config.SecurityConfig;
import edu.fcu.cs1133.payload.GradeUpdateDto;
import edu.fcu.cs1133.payload.StudentProfileDto;
import edu.fcu.cs1133.security.CustomUserDetailsService;
import edu.fcu.cs1133.security.JwtTokenProvider;
import edu.fcu.cs1133.service.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@Import(SecurityConfig.class)
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "grade.view.all")
    public void testGetEnrolledStudents() throws Exception {
        StudentProfileDto studentDto = new StudentProfileDto();
        studentDto.setFirstName("Test");
        when(teacherService.getEnrolledStudents(1)).thenReturn(Collections.singletonList(studentDto));

        mockMvc.perform(get("/api/teachers/1/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Test"));
    }

    @Test
    @WithMockUser(authorities = "grade.update")
    public void testUpdateGrades() throws Exception {
        List<GradeUpdateDto> gradeUpdates = Collections.singletonList(new GradeUpdateDto());
        doNothing().when(teacherService).updateGrades(eq(1), any());

        mockMvc.perform(put("/api/teachers/1/grades").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gradeUpdates)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "wrong.authority")
    public void testGetEnrolledStudents_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/teachers/1/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
