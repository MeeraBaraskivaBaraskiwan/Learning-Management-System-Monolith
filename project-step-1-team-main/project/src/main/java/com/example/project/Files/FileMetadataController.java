package com.example.project.Files;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.example.project.Exceptions.FileStorageException;

@Tag(name = "File Metadata", description = "Operations for managing file metadata and file storage")
@RestController
@RequestMapping("/api/files")
public class FileMetadataController {

    

    private static final Logger logger = LoggerFactory.getLogger(FileMetadataController.class);

    private final FileMetadataService fileMetadataService;

    public FileMetadataController(FileMetadataService fileMetadataService) {
        this.fileMetadataService = fileMetadataService;
    }


     @Operation(summary = "Get all files", description = "Fetch all file metadata with pagination support")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping
    public PagedModel<EntityModel<FileMetadataDTO>> getAllFiles(
            @PageableDefault(size = 5) Pageable pageable
    ) {
        logger.info("Fetching all files with pageable: {}", pageable);
        return fileMetadataService.getAllFiles(pageable);
    }



    @Operation(summary = "Get file by ID", description = "Retrieve file metadata by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File metadata found"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<FileMetadataDTO> getFileById(@PathVariable Long id) {
        logger.info("Fetching file metadata with ID: {}", id);
        return fileMetadataService.getFileById(id);
    }


    @Operation(summary = "Upload assignment file", description = "Upload a file for an assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment file uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "File upload failed")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/assignments/{assignmentId}/upload")
    public EntityModel<FileMetadataDTO> uploadAssignment(
            @PathVariable Long assignmentId,
            @RequestParam("file") MultipartFile file
    ) {
        logger.info("Uploading assignment file for assignment ID: {}", assignmentId);
        try {
            EntityModel<FileMetadataDTO> response = fileMetadataService.storeAssignmentFile(assignmentId, file);
            logger.info("Successfully uploaded assignment file for assignment ID: {}", assignmentId);
            return response;
        } catch (IOException e) {
            logger.error("Failed to upload assignment file for assignment ID: {}", assignmentId, e);
            throw new FileStorageException("Failed to store file for assignmentId=" + assignmentId, e);
        }
    }



    @Operation(summary = "Upload submission file", description = "Upload a file for a submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission file uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "File upload failed")
    })
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submissions/{submissionId}/upload")
    public EntityModel<FileMetadataDTO> uploadSubmission(
            @PathVariable Long submissionId,
            @RequestParam("file") MultipartFile file
    ) {
        logger.info("Uploading submission file for submission ID: {}", submissionId);
        try {
            EntityModel<FileMetadataDTO> response = fileMetadataService.storeSubmissionFile(submissionId, file);
            logger.info("Successfully uploaded submission file for submission ID: {}", submissionId);
            return response;
        } catch (IOException e) {
            logger.error("Failed to upload submission file for submission ID: {}", submissionId, e);
            throw new FileStorageException("Failed to store file for submissionId=" + submissionId, e);
        }
    }



    @Operation(summary = "Upload course content file", description = "Upload a file for course content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course content file uploaded successfully"),
        @ApiResponse(responseCode = "400", description = "File upload failed")
    })
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping("/courseContents/{courseContentId}/upload")
    public EntityModel<FileMetadataDTO> uploadCourseContentFile(
            @PathVariable Long courseContentId,
            @RequestParam("file") MultipartFile file
    ) {
        logger.info("Uploading course content file for courseContent ID: {}", courseContentId);
        try {
            EntityModel<FileMetadataDTO> response = fileMetadataService.storeCourseContentFile(courseContentId, file);
            logger.info("Successfully uploaded course content file for courseContent ID: {}", courseContentId);
            return response;
        } catch (IOException e) {
            logger.error("Failed to upload course content file for courseContent ID: {}", courseContentId, e);
            throw new FileStorageException("Failed to store file for courseContentId=" + courseContentId, e);
        }
    }



    @Operation(summary = "Download assignment file", description = "Download a file associated with an assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Assignment file downloaded successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/assignments/{assignmentId}/{fileId}/download")
    public ResponseEntity<byte[]> downloadAssignmentFile(
            @PathVariable Long assignmentId,
            @PathVariable Long fileId
    ) throws IOException {
        logger.info("Downloading assignment file with ID: {} for assignment ID: {}", fileId, assignmentId);
        return fileMetadataService.loadAssignmentFile(assignmentId, fileId);
    }




    
    @Operation(summary = "Download submission file", description = "Download a file associated with a submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Submission file downloaded successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/submissions/{submissionId}/{fileId}/download")
    public ResponseEntity<byte[]> downloadSubmissionFile(
            @PathVariable Long submissionId,
            @PathVariable Long fileId
    ) throws IOException {
        logger.info("Downloading submission file with ID: {} for submission ID: {}", fileId, submissionId);
        return fileMetadataService.loadSubmissionFile(submissionId, fileId);
    }




    @Operation(summary = "Download course content file", description = "Download a file associated with course content")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course content file downloaded successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/courseContents/{courseContentId}/{fileId}/download")
    public ResponseEntity<byte[]> downloadCourseContentFile(
            @PathVariable Long courseContentId,
            @PathVariable Long fileId
    ) throws IOException {
        logger.info("Downloading course content file with ID: {} for courseContent ID: {}", fileId, courseContentId);
        return fileMetadataService.loadCourseContentFile(courseContentId, fileId);
    }


    @Operation(summary = "Delete file", description = "Delete a file by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "File deleted successfully"),
        @ApiResponse(responseCode = "404", description = "File not found")
    })
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<EntityModel<FileMetadataDTO>> deleteFile(@PathVariable Long fileId) {
        logger.info("Deleting file with ID: {}", fileId);
        return fileMetadataService.deleteFile(fileId);
    }


    @Operation(summary = "Get files by assignment", description = "Retrieve files associated with a specific assignment")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/assignments/{assignmentId}")
    public PagedModel<EntityModel<FileMetadataDTO>> getFilesByAssignment(
            @PathVariable Long assignmentId,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        logger.info("Fetching files for assignment ID: {}", assignmentId);
        return fileMetadataService.getFilesByAssignment(assignmentId, pageable);
    }


    @Operation(summary = "Get files by submission", description = "Retrieve files associated with a specific submission")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Files retrieved successfully")
    })
    @PreAuthorize("hasAnyRole('STUDENT','INSTRUCTOR','ADMIN')")
    @GetMapping("/submissions/{submissionId}")
    public PagedModel<EntityModel<FileMetadataDTO>> getFilesBySubmission(
            @PathVariable Long submissionId,
            @PageableDefault(size = 5) Pageable pageable
    ) {
        logger.info("Fetching files for submission ID: {}", submissionId);
        return fileMetadataService.getFilesBySubmission(submissionId, pageable);
    }
}
