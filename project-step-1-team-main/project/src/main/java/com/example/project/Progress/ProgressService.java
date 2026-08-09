package com.example.project.Progress;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface ProgressService {
    CollectionModel<EntityModel<ProgressDTO>> getProgressByEnrollment(Long enrollmentId);
    ResponseEntity<?> addProgress(ProgressDTO progressDTO);
    ResponseEntity<?> deleteProgress(Long progressId);
    ResponseEntity<?> updateProgress(Long enrollmentId, double progressValue, Long courseContentId);

    CollectionModel<EntityModel<ProgressDTO>> getAllProgress();


}
