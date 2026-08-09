package com.example.project.Assessments.AssessmentsAssignmentSubmission;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Files.FileMetadataDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AssignmentSubmissionDTO {

    private Long id;

    @NotNull(message = "Assignment ID is required")
    private Long assignmentId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @Size(max = 5000, message = "Feedback cannot exceed 5000 characters")
    private String feedback;

    private String submittedAt;

    private List<FileMetadataDTO> files = new ArrayList<>();
}
