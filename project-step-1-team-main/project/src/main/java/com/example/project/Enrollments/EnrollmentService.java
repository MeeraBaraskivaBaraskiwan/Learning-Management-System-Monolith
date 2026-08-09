package com.example.project.Enrollments;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EnrollmentService {
    CollectionModel<EntityModel<EnrollmentDTO>> all(Pageable pageable);
    EntityModel<EnrollmentDTO> one(Long id);
    ResponseEntity<?> enrollStudent(EnrollmentDTO dto);
    ResponseEntity<?> updateEnrollmentProgress(Long enrollmentId, Long courseContentId);
    ResponseEntity<?> removeEnrollment(Long id);
    CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByStudent(Long studentId);
    CollectionModel<EntityModel<EnrollmentDTO>> getEnrollmentsByCourse(Long courseId);
    List<EnrollmentDTO> getPlainEnrollmentsByStudent(Long studentId);

}
