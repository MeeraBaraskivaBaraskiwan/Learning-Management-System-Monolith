package com.example.project.Files;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Page<FileMetadata> findAll(Pageable pageable);

    Page<FileMetadata> findByAssignmentDetails_Id(Long assignmentDetailsId, Pageable pageable);

    Page<FileMetadata> findByAssignmentSubmission_Id(Long assignmentSubmissionId, Pageable pageable);

    Page<FileMetadata> findByCourseContent_Id(Long courseContentId, Pageable pageable);

    Optional<FileMetadata> findByStoredFilename(String storedFilename);
}
