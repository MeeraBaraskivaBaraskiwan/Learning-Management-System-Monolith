package com.example.project.Progress;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDTO {
    private Long id;

    @NotNull(message = "Enrollment ID cannot be null")
    private Long enrollmentId;

    @NotNull(message = "Course content ID cannot be null")
    private Long courseContentId;
    
    @Min(value = 1, message = "Progress must be at least 1")
    @Max(value = 100, message = "Progress must be at most 100")
    private double progress;

    @Min(value = 0, message = "Completed tasks must be at least 0")
    private double completedTasks;

    @Min(value = 1, message = "Total tasks must be at least 1")
    private double totalTasks;

    private LocalDateTime updatedAt;
}

