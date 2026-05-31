package com.microservice.course.service;

import com.microservice.course.client.StudentClient;
import com.microservice.course.dto.StudentDTO;
import com.microservice.course.entities.Course;
import com.microservice.course.http.response.StudentByCourseResponse;
import com.microservice.course.persistence.ICourseRepository;
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
class CourseServiceImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private StudentClient studentClient;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void testFindAllCourses() {
        Course course = Course.builder()
                .id(1L)
                .name("Microservicios")
                .teacher("Profesor Juan")
                .build();

        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<Course> result = courseService.findAll();

        assertEquals(1, result.size());
        assertEquals("Microservicios", result.get(0).getName());

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testFindCourseById() {
        Course course = Course.builder()
                .id(1L)
                .name("Base de Datos")
                .teacher("Profesor Luis")
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Base de Datos", result.getName());

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testSaveCourse() {
        Course course = Course.builder()
                .name("Programacion Web")
                .teacher("Profesora Ana")
                .build();

        courseService.save(course);

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testFindStudentsByIdCourse() {
        Course course = Course.builder()
                .id(1L)
                .name("Microservicios")
                .teacher("Profesor Juan")
                .build();

        StudentDTO studentDTO = StudentDTO.builder()
                .name("Eduardo")
                .lastname("Castillo")
                .email("eduardo@gmail.com")
                .courseId(1L)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentClient.findAllStudentByCourse(1L)).thenReturn(List.of(studentDTO));

        StudentByCourseResponse response = courseService.findStudentsByIdCourse(1L);

        assertEquals("Microservicios", response.getCourseName());
        assertEquals("Profesor Juan", response.getTeacher());
        assertEquals(1, response.getStudentDTOList().size());

        verify(courseRepository, times(1)).findById(1L);
        verify(studentClient, times(1)).findAllStudentByCourse(1L);
    }

    @Test
    void testDeleteCourseWhenExists() {
        Course course = Course.builder()
                .id(1L)
                .name("Redes")
                .teacher("Profesor Mario")
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = courseService.delete(1L);

        assertTrue(result);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}
