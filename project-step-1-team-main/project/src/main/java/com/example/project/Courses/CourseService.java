package com.example.project.Courses;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface CourseService {
    CollectionModel<EntityModel<CourseDTO>> all(Pageable pageable);
    ResponseEntity<?> newCourse(CourseDTO newCourse);
    EntityModel<CourseDTO> one(Long id);
    ResponseEntity<?> updateCourse(CourseDTO updatedCourse, Long id);
    ResponseEntity<?> deleteCourse(Long id);
}
