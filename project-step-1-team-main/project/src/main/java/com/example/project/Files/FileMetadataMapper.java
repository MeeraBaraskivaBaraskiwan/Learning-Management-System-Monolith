package com.example.project.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Assessments.AssessmentsAssignmentSubmission.AssignmentSubmission;
import com.example.project.CourseContents.CourseContent;

@Component
public class FileMetadataMapper {

    private static final Logger logger = LoggerFactory.getLogger(FileMetadataMapper.class);

    public static FileMetadataDTO toDTO(FileMetadata fileMetadata) {
        if (fileMetadata == null) {
            logger.warn("Attempted to map a null FileMetadata entity to DTO");
            return null;
        }

        logger.info("Mapping FileMetadata entity with ID: {} to DTO", fileMetadata.getId());
        FileMetadataDTO dto = new FileMetadataDTO();
        dto.setId(fileMetadata.getId());
        dto.setOriginalFilename(fileMetadata.getOriginalFilename());
        dto.setStoredFilename(fileMetadata.getStoredFilename());
        dto.setFileExtension(fileMetadata.getFileExtension());
        dto.setFileSize(fileMetadata.getFileSize());

        // Assign the correct ownership type and owner ID
        if (fileMetadata.getAssignmentDetails() != null) {
            dto.setOwnerType(FileOwnerType.ASSIGNMENT_DETAILS);
            dto.setOwnerId(fileMetadata.getAssignmentDetails().getId());
        } else if (fileMetadata.getAssignmentSubmission() != null) {
            dto.setOwnerType(FileOwnerType.ASSIGNMENT_SUBMISSION);
            dto.setOwnerId(fileMetadata.getAssignmentSubmission().getId());
        } else if (fileMetadata.getCourseContent() != null) {
            dto.setOwnerType(FileOwnerType.COURSE_CONTENT);
            dto.setOwnerId(fileMetadata.getCourseContent().getId());
        } else {
            logger.warn("FileMetadata entity with ID: {} has no associated owner", fileMetadata.getId());
        }

        logger.info("Successfully mapped FileMetadata entity with ID: {} to DTO", fileMetadata.getId());
        return dto;
    }

    public static <T> FileMetadata toEntity(FileMetadataDTO dto, T ownerEntity) {
        if (dto == null || ownerEntity == null) {
            logger.warn("Attempted to map a null FileMetadataDTO or owner entity to FileMetadata");
            return null;
        }

        logger.info("Mapping FileMetadataDTO with ID: {} to FileMetadata entity", dto.getId());
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setOriginalFilename(dto.getOriginalFilename());
        fileMetadata.setStoredFilename(dto.getStoredFilename());
        fileMetadata.setFileExtension(dto.getFileExtension());
        fileMetadata.setFileSize(dto.getFileSize());

        if (ownerEntity instanceof AssignmentDetails assignmentDetails && dto.getOwnerType() == FileOwnerType.ASSIGNMENT_DETAILS) {
            fileMetadata.setAssignmentDetails(assignmentDetails);
        } else if (ownerEntity instanceof AssignmentSubmission assignmentSubmission && dto.getOwnerType() == FileOwnerType.ASSIGNMENT_SUBMISSION) {
            fileMetadata.setAssignmentSubmission(assignmentSubmission);
        } else if (ownerEntity instanceof CourseContent courseContent && dto.getOwnerType() == FileOwnerType.COURSE_CONTENT) {
            fileMetadata.setCourseContent(courseContent);
        } else {
            logger.error("Invalid ownership type or mismatched owner entity for FileMetadataDTO with ID: {}", dto.getId());
            throw new IllegalArgumentException("Invalid ownership type or mismatched owner entity.");
        }

        logger.info("Successfully mapped FileMetadataDTO with ID: {} to FileMetadata entity", dto.getId());
        return fileMetadata;
    }
}

