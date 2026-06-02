package com.microservice.course.service;

import com.microservice.course.client.StudentClient;
import com.microservice.course.dto.StudentDTO;
import com.microservice.course.entities.Course;
import com.microservice.course.http.response.StudentByCourseResponse;
import com.microservice.course.persistence.ICourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseServiceImpl implements ICourseService{

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private StudentClient studentClient;

    @Override
    public List<Course> findAll() {
        logger.info("Consultando todos los cursos");
        return (List<Course>) courseRepository.findAll();
    }

    @Override
    public Course findById(Long id) {
        logger.info("Buscando curso con ID: {}", id);

        Course course = courseRepository.findById(id).orElse(null);

        if (course == null) {
            logger.warn("No se encontró el curso con ID: {}", id);
            return null;
        }

        logger.info("Curso encontrado: {}", course.getName());
        return course;
    }

    @Override
    public void save(Course course) {
        logger.info("Guardando nuevo curso: {}", course.getName());
        courseRepository.save(course);
        logger.info("Curso guardado correctamente");
    }

    @Override
    public StudentByCourseResponse findStudentsByIdCourse(Long idCourse) {
        logger.info("Consultando estudiantes del curso con ID: {}", idCourse);

        Course course = courseRepository.findById(idCourse).orElse(null);

        if (course == null) {
            logger.warn("No se encontró el curso con ID: {}", idCourse);
            return null;
        }

        List<StudentDTO> studentDTOList = studentClient.findAllStudentByCourse(idCourse);

        logger.info("Consulta de estudiantes por curso realizada correctamente");

        return StudentByCourseResponse.builder()
                .courseName(course.getName())
                .teacher(course.getTeacher())
                .studentDTOList(studentDTOList)
                .build();
    }

    @Override
    public Course update(Long id, Course course) {
        logger.info("Intentando actualizar curso con ID: {}", id);

        Optional<Course> existingCourse = courseRepository.findById(id);

        if (existingCourse.isPresent()) {
            Course updatedCourse = existingCourse.get();
            updatedCourse.setName(course.getName());

            Course savedCourse = courseRepository.save(updatedCourse);

            logger.info("Curso actualizado correctamente con ID: {}", id);
            return savedCourse;
        }

        logger.warn("No se pudo actualizar. No existe curso con ID: {}", id);
        return null;
    }

    @Override
    public boolean delete(Long id) {
        logger.info("Intentando eliminar curso con ID: {}", id);

        Course course = courseRepository.findById(id).orElse(null);

        if (course != null) {
            courseRepository.deleteById(id);
            logger.info("Curso eliminado correctamente con ID: {}", id);
            return true;
        }

        logger.warn("No se pudo eliminar. No existe curso con ID: {}", id);
        return false;
    }
}
