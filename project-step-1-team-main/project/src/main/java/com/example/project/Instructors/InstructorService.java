package com.example.project.Instructors;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface InstructorService {
    CollectionModel<EntityModel<InstructorDTO>> all(Pageable pageable);

    ResponseEntity<?> newInstructor(InstructorDTO newInstructor);

    EntityModel<InstructorDTO> one(Long id);

    ResponseEntity<?> updateInstructor(InstructorDTO updatedInstructor, Long id);

    ResponseEntity<?> deleteInstructor(Long id);

    // ←– Add this:
    EntityModel<InstructorDTO> findByUserId(Long userId);

}
