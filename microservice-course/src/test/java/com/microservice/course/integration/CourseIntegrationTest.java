package com.microservice.course.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.course.client.StudentClient;
import com.microservice.course.entities.Course;
import com.microservice.course.persistence.ICourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentClient studentClient;

    @BeforeEach
    void setUp() {
        courseRepository.deleteAll();
    }

    @Test
    void testCreateCourse() throws Exception {
        Course course = Course.builder()
                .name("Microservicios")
                .teacher("Profesor Juan")
                .build();

        mockMvc.perform(post("/api/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated());
    }

    @Test
    void testFindAllCourses() throws Exception {
        Course course = Course.builder()
                .name("Base de Datos")
                .teacher("Profesor Luis")
                .build();

        courseRepository.save(course);

        mockMvc.perform(get("/api/course/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Base de Datos")));
    }

    @Test
    void testFindCourseById() throws Exception {
        Course course = Course.builder()
                .name("Redes")
                .teacher("Profesor Mario")
                .build();

        Course savedCourse = courseRepository.save(course);

        mockMvc.perform(get("/api/course/search/" + savedCourse.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Redes")));
    }

    @Test
    void testDeleteCourse() throws Exception {
        Course course = Course.builder()
                .name("Programacion Web")
                .teacher("Profesora Ana")
                .build();

        Course savedCourse = courseRepository.save(course);

        mockMvc.perform(delete("/api/course/delete/" + savedCourse.getId()))
                .andExpect(status().isNoContent());
    }
}
