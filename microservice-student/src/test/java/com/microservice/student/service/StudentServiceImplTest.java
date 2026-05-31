package com.microservice.student.service;

import com.microservice.student.entities.Student;
import com.microservice.student.persistence.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void testFindAllStudents() {
        Student student = Student.builder()
                .id(1L)
                .name("Eduardo")
                .lastname("Castillo")
                .email("eduardo@gmail.com")
                .courseId(1L)
                .build();

        when(studentRepository.findAll()).thenReturn(List.of(student));

        List<Student> result = studentService.findAll();

        assertEquals(1, result.size());
        assertEquals("Eduardo", result.get(0).getName());

        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testFindStudentById() {
        Student student = Student.builder()
                .id(1L)
                .name("Eduardo")
                .lastname("Castillo")
                .email("eduardo@gmail.com")
                .courseId(1L)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Eduardo", result.getName());

        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveStudent() {
        Student student = Student.builder()
                .name("Carlos")
                .lastname("Lopez")
                .email("carlos@gmail.com")
                .courseId(1L)
                .build();

        studentService.save(student);

        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testFindStudentsByCourse() {
        Student student = Student.builder()
                .id(1L)
                .name("Luis")
                .lastname("Perez")
                .email("luis@gmail.com")
                .courseId(5L)
                .build();

        when(studentRepository.findAllStudent(5L)).thenReturn(List.of(student));

        List<Student> result = studentService.findByIdCourse(5L);

        assertEquals(1, result.size());
        assertEquals(5L, result.get(0).getCourseId());

        verify(studentRepository, times(1)).findAllStudent(5L);
    }

    @Test
    void testDeleteStudentWhenExists() {
        Student student = Student.builder()
                .id(1L)
                .name("Ana")
                .lastname("Martinez")
                .email("ana@gmail.com")
                .courseId(2L)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        boolean result = studentService.delete(1L);

        assertTrue(result);
        verify(studentRepository, times(1)).deleteById(1L);
    }
}
