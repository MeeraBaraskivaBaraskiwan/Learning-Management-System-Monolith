package com.example.project.CourseInstructors;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface CourseInstructorService {
    CollectionModel<EntityModel<CourseInstructorDTO>> all(Pageable pageable);
    EntityModel<CourseInstructorDTO> one(Long id);
    ResponseEntity<?> assignInstructorToCourse(CourseInstructorDTO dto);
    ResponseEntity<?> removeInstructorFromCourse(Long id);
    CollectionModel<EntityModel<CourseInstructorDTO>> getInstructorsByCourse(Long courseId);
    CollectionModel<EntityModel<CourseInstructorDTO>> getCoursesByInstructor(Long instructorId);
}
