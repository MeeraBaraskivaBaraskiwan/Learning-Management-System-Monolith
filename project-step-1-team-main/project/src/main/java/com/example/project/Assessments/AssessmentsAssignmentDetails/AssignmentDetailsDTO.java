package com.example.project.Assessments.AssessmentsAssignmentDetails;

import java.util.ArrayList;
import java.util.List;

import com.example.project.Files.FileMetadataDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class AssignmentDetailsDTO {

    private Long id;

    @NotNull(message = "Assessment ID is required")
    private Long assessmentId;

    @NotNull(message = "Total score is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total score cannot be negative")
    private Double totalScore;

    @Size(max = 2000, message = "Notes cannot exceed 2000 characters")
    private String notes;

    private boolean published;
    private List<FileMetadataDTO> files = new ArrayList<>();
}

