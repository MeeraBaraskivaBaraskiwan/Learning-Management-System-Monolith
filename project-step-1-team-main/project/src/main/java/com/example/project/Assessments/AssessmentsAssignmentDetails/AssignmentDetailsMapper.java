package com.example.project.Assessments.AssessmentsAssignmentDetails;

import com.example.project.Assessments.Assessments_Assessment.Assessment;
import com.example.project.Files.FileMetadataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AssignmentDetailsMapper {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentDetailsMapper.class);

    public static AssignmentDetailsDTO toDTO(AssignmentDetails assignmentDetails) {
        if (assignmentDetails == null) {
            logger.warn("Attempted to map a null AssignmentDetails to AssignmentDetailsDTO");
            return null;
        }

        logger.info("Mapping AssignmentDetails to AssignmentDetailsDTO with ID: {}", assignmentDetails.getId());

        AssignmentDetailsDTO dto = new AssignmentDetailsDTO();
        dto.setId(assignmentDetails.getId());
        dto.setAssessmentId(assignmentDetails.getAssessment().getId());
        dto.setTotalScore(assignmentDetails.getTotalScore());
        dto.setNotes(assignmentDetails.getNotes());
        dto.setPublished(assignmentDetails.isPublished());

        // Populate the 'files' list from the entity’s fileMetadataList
        dto.setFiles(
            assignmentDetails.getFileMetadataList()
                             .stream()
                             .map(FileMetadataMapper::toDTO)
                             .collect(Collectors.toList())
        );

        logger.debug("Mapped AssignmentDetailsDTO: {}", dto);
        return dto;
    }

    public static AssignmentDetails toEntity(AssignmentDetailsDTO dto, Assessment assessment) {
        if (dto == null) {
            logger.warn("Attempted to map a null AssignmentDetailsDTO to AssignmentDetails");
            return null;
        }
        if (assessment == null) {
            logger.error("Assessment is null while mapping AssignmentDetailsDTO to AssignmentDetails");
            return null;
        }

        logger.info("Mapping AssignmentDetailsDTO to AssignmentDetails for Assessment ID: {}", assessment.getId());

        AssignmentDetails entity = new AssignmentDetails();
        entity.setId(dto.getId());
        entity.setAssessment(assessment);
        entity.setTotalScore(dto.getTotalScore());
        entity.setNotes(dto.getNotes());
        entity.setPublished(dto.isPublished());

        // Note: fileMetadataList is managed separately via FileMetadataService

        logger.debug("Mapped AssignmentDetails: {}", entity);
        return entity;
    }
}
