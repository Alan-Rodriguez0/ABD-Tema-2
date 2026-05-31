package com.microservice.student.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.student.entities.Student;
import com.microservice.student.persistence.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class StudentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        Student student = Student.builder()
                .name("Eduardo")
                .lastname("Castillo")
                .email("eduardo@gmail.com")
                .courseId(1L)
                .build();

        mockMvc.perform(post("/api/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated());
    }

    @Test
    void testFindAllStudents() throws Exception {
        Student student = Student.builder()
                .name("Carlos")
                .lastname("Lopez")
                .email("carlos@gmail.com")
                .courseId(1L)
                .build();

        studentRepository.save(student);

        mockMvc.perform(get("/api/student/all"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Carlos")));
    }

    @Test
    void testFindStudentById() throws Exception {
        Student student = Student.builder()
                .name("Luis")
                .lastname("Perez")
                .email("luis@gmail.com")
                .courseId(2L)
                .build();

        Student savedStudent = studentRepository.save(student);

        mockMvc.perform(get("/api/student/search/" + savedStudent.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Luis")));
    }

    @Test
    void testDeleteStudent() throws Exception {
        Student student = Student.builder()
                .name("Ana")
                .lastname("Martinez")
                .email("ana@gmail.com")
                .courseId(3L)
                .build();

        Student savedStudent = studentRepository.save(student);

        mockMvc.perform(delete("/api/student/delete/" + savedStudent.getId()))
                .andExpect(status().isNoContent());
    }
}

