package com.example.project.Files;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileMetadataService {

    
    PagedModel<EntityModel<FileMetadataDTO>> getAllFiles(Pageable pageable);

    EntityModel<FileMetadataDTO> getFileById(Long id);

    EntityModel<FileMetadataDTO> storeAssignmentFile(Long assignmentId, MultipartFile file) throws IOException;

    EntityModel<FileMetadataDTO> storeSubmissionFile(Long submissionId, MultipartFile file) throws IOException;

    EntityModel<FileMetadataDTO> storeCourseContentFile(Long courseContentId, MultipartFile file) throws IOException;

    ResponseEntity<byte[]> loadAssignmentFile(Long assignmentId, Long fileId) throws IOException;

    ResponseEntity<byte[]> loadSubmissionFile(Long submissionId, Long fileId) throws IOException;

    ResponseEntity<byte[]> loadCourseContentFile(Long courseContentId, Long fileId) throws IOException;

    ResponseEntity<EntityModel<FileMetadataDTO>> deleteFile(Long fileId);

    PagedModel<EntityModel<FileMetadataDTO>> getFilesByAssignment(Long assignmentId, Pageable pageable);

    PagedModel<EntityModel<FileMetadataDTO>> getFilesBySubmission(Long submissionId, Pageable pageable);

    PagedModel<EntityModel<FileMetadataDTO>> getFilesByCourseContent(Long courseContentId, Pageable pageable);

}
