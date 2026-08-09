package com.example.project.Files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetailsRepository;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmissionRepository;
import com.example.project.CourseContents.CourseContent;
import com.example.project.CourseContents.CourseContentRepository;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Exceptions.ValidationException;

import jakarta.annotation.PostConstruct;

@Service
public class FileMetadataServiceImpl implements FileMetadataService {

    private static final Logger logger = LoggerFactory.getLogger(FileMetadataServiceImpl.class);

    private final FileMetadataRepository fileMetadataRepository;
    private final FileMetadataAssembler fileMetadataAssembler;
    private final PagedResourcesAssembler<FileMetadataDTO> pagedAssembler;

    private final AssignmentDetailsRepository assignmentDetailsRepository;
    private final AssignmentSubmissionRepository assignmentSubmissionRepository;
    private final CourseContentRepository courseContentRepository;

    @Value("${file.upload.assignments-dir}")
    private String assignmentsDir;

    @Value("${file.upload.submissions-dir}")
    private String submissionsDir;

    @Value("${file.upload.course-content-dir}")
    private String courseContentDir;

    @Value("${file.upload.max-size}")
    private long maxFileSize;

    public FileMetadataServiceImpl(
            FileMetadataRepository fileMetadataRepository,
            FileMetadataAssembler fileMetadataAssembler,
            PagedResourcesAssembler<FileMetadataDTO> pagedAssembler,
            AssignmentDetailsRepository assignmentDetailsRepository,
            AssignmentSubmissionRepository assignmentSubmissionRepository,
            CourseContentRepository courseContentRepository
    ) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileMetadataAssembler = fileMetadataAssembler;
        this.pagedAssembler = pagedAssembler;
        this.assignmentDetailsRepository = assignmentDetailsRepository;
        this.assignmentSubmissionRepository = assignmentSubmissionRepository;
        this.courseContentRepository = courseContentRepository;
    }

    @PostConstruct
    public void init() {
        createDirectory(assignmentsDir);
        createDirectory(submissionsDir);
        createDirectory(courseContentDir);
    }

    private void createDirectory(String dirPath) {
    Path path = Paths.get(dirPath);
    try {
        Files.createDirectories(path);
        logger.info("Directory ensured: {}", dirPath);
    } catch (IOException e) {
        logger.error("Failed to create directory: {}", dirPath, e);
        throw new RuntimeException("Could not create directory: " + dirPath, e);
    }
    }
    @Override
    public PagedModel<EntityModel<FileMetadataDTO>> getAllFiles(Pageable pageable) {
        logger.info("Fetching all files with pageable: {}", pageable);
        Page<FileMetadataDTO> page = fileMetadataRepository
                .findAll(pageable)
                .map(FileMetadataMapper::toDTO);
        logger.info("Successfully fetched {} files", page.getTotalElements());
        return pagedAssembler.toModel(page, fileMetadataAssembler);
    }

    @Override
    public EntityModel<FileMetadataDTO> getFileById(Long id) {
        logger.info("Fetching file metadata with ID: {}", id);
        FileMetadata metadata = fileMetadataRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("File metadata not found for ID: {}", id);
                    return new ResourceNotFoundException("File metadata not found for ID: " + id);
                });
        logger.info("Successfully fetched file metadata with ID: {}", id);
        return fileMetadataAssembler.toModel(FileMetadataMapper.toDTO(metadata));
    }

    @Override
    public EntityModel<FileMetadataDTO> storeAssignmentFile(Long assignmentId, MultipartFile file) throws IOException {
        logger.info("Storing assignment file for assignment ID: {}", assignmentId);
        if (file.isEmpty()) {
            logger.warn("Attempted to store an empty file for assignment ID: {}", assignmentId);
            throw new ValidationException("Cannot store an empty file.");
        }

        AssignmentDetails assignmentDetails = assignmentDetailsRepository.findById(assignmentId)
                .orElseThrow(() -> {
                    logger.error("AssignmentDetails not found for ID: {}", assignmentId);
                    return new ResourceNotFoundException("AssignmentDetails with ID " + assignmentId + " not found");
                });

        FileMetadata savedMetadata = storeFileInternal(file, assignmentDetails, null, null);
        logger.info("Successfully stored assignment file for assignment ID: {}", assignmentId);
        return fileMetadataAssembler.toModel(FileMetadataMapper.toDTO(savedMetadata));
    }

    @Override
    public EntityModel<FileMetadataDTO> storeSubmissionFile(Long submissionId, MultipartFile file) throws IOException {
        logger.info("Storing submission file for submission ID: {}", submissionId);
        if (file.isEmpty()) {
            logger.warn("Attempted to store an empty file for submission ID: {}", submissionId);
            throw new ValidationException("Cannot store an empty file.");
        }

        AssignmentSubmission submission = assignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> {
                    logger.error("AssignmentSubmission not found for ID: {}", submissionId);
                    return new ResourceNotFoundException("AssignmentSubmission with ID " + submissionId + " not found");
                });

        FileMetadata savedMetadata = storeFileInternal(file, null, submission, null);
        logger.info("Successfully stored submission file for submission ID: {}", submissionId);
        return fileMetadataAssembler.toModel(FileMetadataMapper.toDTO(savedMetadata));
    }

    private FileMetadata storeFileInternal(MultipartFile file, AssignmentDetails assignmentDetails, AssignmentSubmission submission, CourseContent courseContent) throws IOException {
        logger.info("Storing file: {}", file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        String fileExtension = extractFileExtension(originalFilename);
        String storedFilename = UUID.randomUUID().toString() + fileExtension;
        String directoryPath;

        if (assignmentDetails != null) {
            directoryPath = assignmentsDir;
        } else if (submission != null) {
            directoryPath = submissionsDir;
        } else if (courseContent != null) {
            directoryPath = courseContentDir;
        } else {
            logger.error("File must belong to one of: AssignmentDetails, AssignmentSubmission, or CourseContent.");
            throw new ValidationException("File must belong to one of: AssignmentDetails, AssignmentSubmission, or CourseContent.");
        }

        Path filePath = Paths.get(directoryPath, storedFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        if (file.getSize() > maxFileSize) {
            logger.error("File size exceeds maximum allowed limit ({} bytes): {}", maxFileSize, file.getSize());
            throw new ValidationException("File size exceeds maximum allowed limit (" + maxFileSize + " bytes).");
        }

        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setOriginalFilename(originalFilename);
        fileMetadata.setStoredFilename(storedFilename);
        fileMetadata.setFileExtension(fileExtension);
        fileMetadata.setFileSize(file.getSize());

        if (assignmentDetails != null) {
            fileMetadata.setAssignmentDetails(assignmentDetails);
        } else if (submission != null) {
            fileMetadata.setAssignmentSubmission(submission);
        } else {
            fileMetadata.setCourseContent(courseContent);
        }

        logger.info("File stored successfully: {}", storedFilename);
        return fileMetadataRepository.save(fileMetadata);
    }

    @Override
    public ResponseEntity<byte[]> loadAssignmentFile(Long assignmentId, Long fileId) throws IOException {
        logger.info("Loading assignment file with ID: {} for assignment ID: {}", fileId, assignmentId);
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> {
                    logger.error("File metadata not found for ID: {}", fileId);
                    return new ResourceNotFoundException("File metadata not found for ID: " + fileId);
                });

        if (fileMetadata.getAssignmentDetails() == null || !fileMetadata.getAssignmentDetails().getId().equals(assignmentId)) {
            logger.error("File does not belong to assignment with ID: {}", assignmentId);
            throw new ValidationException("File does not belong to assignment with ID " + assignmentId);
        }

        return loadFileInternal(fileMetadata, assignmentsDir);
    }

    @Override
    public ResponseEntity<byte[]> loadSubmissionFile(Long submissionId, Long fileId) throws IOException {
        logger.info("Loading submission file with ID: {} for submission ID: {}", fileId, submissionId);
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> {
                    logger.error("File metadata not found for ID: {}", fileId);
                    return new ResourceNotFoundException("File metadata not found for ID: " + fileId);
                });

        if (fileMetadata.getAssignmentSubmission() == null || !fileMetadata.getAssignmentSubmission().getId().equals(submissionId)) {
            logger.error("File does not belong to submission with ID: {}", submissionId);
            throw new ValidationException("File does not belong to submission with ID " + submissionId);
        }

        return loadFileInternal(fileMetadata, submissionsDir);
    }

    private ResponseEntity<byte[]> loadFileInternal(FileMetadata fileMetadata, String baseDirectory) throws IOException {
        logger.info("Loading file from directory: {} with stored filename: {}", baseDirectory, fileMetadata.getStoredFilename());
        Path filePath = Paths.get(baseDirectory, fileMetadata.getStoredFilename());

        if (!Files.exists(filePath)) {
            logger.error("File not found on disk: {}", fileMetadata.getStoredFilename());
            throw new ResourceNotFoundException("File not found on disk: " + fileMetadata.getStoredFilename());
        }

        byte[] fileData = Files.readAllBytes(filePath);
        String contentType = Files.probeContentType(filePath);

        logger.info("File loaded successfully: {}", fileMetadata.getStoredFilename());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getOriginalFilename() + "\"")
                .body(fileData);
    }

    @Override
    public ResponseEntity<EntityModel<FileMetadataDTO>> deleteFile(Long fileId) {
        logger.info("Deleting file with ID: {}", fileId);
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> {
                    logger.error("File metadata not found for ID: {}", fileId);
                    return new ResourceNotFoundException("File metadata not found for ID: " + fileId);
                });

        String directoryPath;
        if (fileMetadata.getAssignmentDetails() != null) {
            directoryPath = assignmentsDir;
        } else if (fileMetadata.getAssignmentSubmission() != null) {
            directoryPath = submissionsDir;
        } else if (fileMetadata.getCourseContent() != null) {
            directoryPath = courseContentDir;
        } else {
            logger.error("File has no associated owner.");
            throw new ValidationException("File has no associated owner.");
        }

        Path filePath = Paths.get(directoryPath, fileMetadata.getStoredFilename());
        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("File deleted from disk: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Failed to delete file from disk: {}", filePath, e);
            throw new ValidationException("Failed to delete file from disk: " + filePath);
        }

        fileMetadataRepository.delete(fileMetadata);
        logger.info("File metadata deleted successfully for ID: {}", fileId);
        return ResponseEntity.noContent().build();
    }

    private String extractFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        int lastDot = filename.lastIndexOf(".");
        return filename.substring(lastDot).toLowerCase();
    }

    @Override
    public PagedModel<EntityModel<FileMetadataDTO>> getFilesByAssignment(Long assignmentId, Pageable pageable) {
        logger.info("Fetching files for assignment ID: {}", assignmentId);
        Page<FileMetadataDTO> page = fileMetadataRepository
                .findByAssignmentDetails_Id(assignmentId, pageable)
                .map(FileMetadataMapper::toDTO);
        logger.info("Successfully fetched {} files for assignment ID: {}", page.getTotalElements(), assignmentId);
        return pagedAssembler.toModel(page, fileMetadataAssembler);
    }

    @Override
    public PagedModel<EntityModel<FileMetadataDTO>> getFilesBySubmission(Long submissionId, Pageable pageable) {
        logger.info("Fetching files for submission ID: {}", submissionId);
        Page<FileMetadataDTO> page = fileMetadataRepository
                .findByAssignmentSubmission_Id(submissionId, pageable)
                .map(FileMetadataMapper::toDTO);
        logger.info("Successfully fetched {} files for submission ID: {}", page.getTotalElements(), submissionId);
        return pagedAssembler.toModel(page, fileMetadataAssembler);
    }

    @Override
    public PagedModel<EntityModel<FileMetadataDTO>> getFilesByCourseContent(Long courseContentId, Pageable pageable) {
        logger.info("Fetching files for course content ID: {}", courseContentId);
        Page<FileMetadataDTO> page = fileMetadataRepository
                .findByCourseContent_Id(courseContentId, pageable)
                .map(FileMetadataMapper::toDTO);
        logger.info("Successfully fetched {} files for course content ID: {}", page.getTotalElements(), courseContentId);
        return pagedAssembler.toModel(page, fileMetadataAssembler);
    }

    @Override
    public EntityModel<FileMetadataDTO> storeCourseContentFile(Long courseContentId, MultipartFile file) throws IOException {
        logger.info("Storing course content file for course content ID: {}", courseContentId);
        if (file.isEmpty()) {
            logger.warn("Attempted to store an empty file for course content ID: {}", courseContentId);
            throw new ValidationException("Cannot store an empty file.");
        }

        CourseContent courseContent = courseContentRepository.findById(courseContentId)
                .orElseThrow(() -> {
                    logger.error("CourseContent not found for ID: {}", courseContentId);
                    return new ResourceNotFoundException("CourseContent with ID " + courseContentId + " not found");
                });

        if (file.getSize() > maxFileSize) {
            logger.error("File size exceeds the maximum allowed limit ({} bytes): {}", maxFileSize, file.getSize());
            throw new ValidationException("File size exceeds the maximum allowed limit (" + maxFileSize + " bytes).");
        }

        FileMetadata savedMetadata = storeFileInternal(file, null, null, courseContent);
        logger.info("Successfully stored course content file for course content ID: {}", courseContentId);
        return fileMetadataAssembler.toModel(FileMetadataMapper.toDTO(savedMetadata));
    }

    @Override
    public ResponseEntity<byte[]> loadCourseContentFile(Long courseContentId, Long fileId) throws IOException {
        logger.info("Loading course content file with ID: {} for course content ID: {}", fileId, courseContentId);
        FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
                .orElseThrow(() -> {
                    logger.error("File metadata not found for ID: {}", fileId);
                    return new ResourceNotFoundException("File metadata not found for ID: " + fileId);
                });

        if (fileMetadata.getCourseContent() == null || !fileMetadata.getCourseContent().getId().equals(courseContentId)) {
            logger.error("File does not belong to course content with ID: {}", courseContentId);
            throw new ValidationException("File does not belong to course content with ID " + courseContentId);
        }

        Path filePath = Paths.get(courseContentDir, fileMetadata.getStoredFilename());
        if (!Files.exists(filePath)) {
            logger.error("File not found on disk: {}", fileMetadata.getStoredFilename());
            throw new ResourceNotFoundException("File not found on disk: " + fileMetadata.getStoredFilename());
        }

        byte[] fileData = Files.readAllBytes(filePath);
        String contentType = Files.probeContentType(filePath);

        logger.info("File loaded successfully: {}", fileMetadata.getStoredFilename());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getOriginalFilename() + "\"")
                .body(fileData);
    }
}

