// project/src/main/java/com/example/project/Assessments/AssessmentsAssignmentSubmission/AssignmentSubmissionMapper.java
package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Assessments.AssessmentsAssignmentDetails.AssignmentDetails;
import com.example.project.Files.FileMetadataMapper;
import com.example.project.Students.Student;

@Component
public class AssignmentSubmissionMapper {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentSubmissionMapper.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static AssignmentSubmissionDTO toDTO(AssignmentSubmission submission) {
        if (submission == null) {
            logger.warn("Attempted to map a null AssignmentSubmission to AssignmentSubmissionDTO");
            return null;
        }

        logger.info("Mapping AssignmentSubmission to AssignmentSubmissionDTO with ID: {}", submission.getId());

        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setId(submission.getId());
        dto.setAssignmentId(submission.getAssignment().getId());
        dto.setStudentId(submission.getStudent().getId());
        dto.setFeedback(submission.getFeedback());

        if (submission.getSubmittedAt() != null) {
            dto.setSubmittedAt(submission.getSubmittedAt().format(FORMATTER));
            logger.debug("Formatted submittedAt for AssignmentSubmissionDTO with ID: {}", submission.getId());
        }

        // ← New: populate the files list
        dto.setFiles(
            submission.getFileMetadataList().stream()
                .map(FileMetadataMapper::toDTO)
                .collect(Collectors.toList())
        );

        logger.debug("Mapped AssignmentSubmissionDTO with files: {}", dto);
        return dto;
    }

    public static AssignmentSubmission toEntity(AssignmentSubmissionDTO dto, AssignmentDetails assignment, Student student) {
        if (dto == null) {
            logger.warn("Attempted to map a null AssignmentSubmissionDTO to AssignmentSubmission");
            return null;
        }
        if (assignment == null) {
            logger.error("AssignmentDetails is null while mapping AssignmentSubmissionDTO to AssignmentSubmission");
            return null;
        }
        if (student == null) {
            logger.error("Student is null while mapping AssignmentSubmissionDTO to AssignmentSubmission");
            return null;
        }

        logger.info("Mapping AssignmentSubmissionDTO to AssignmentSubmission for Assignment ID: {} and Student ID: {}", assignment.getId(), student.getId());

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setFeedback(dto.getFeedback() != null ? dto.getFeedback() : "");
        submission.setSubmittedAt(LocalDateTime.now());

        logger.debug("Mapped AssignmentSubmission: {}", submission);
        return submission;
    }
}
