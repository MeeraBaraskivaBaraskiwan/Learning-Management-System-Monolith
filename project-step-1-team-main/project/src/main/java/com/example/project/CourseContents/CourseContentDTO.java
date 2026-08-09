package com.example.project.CourseContents;

import java.time.LocalDateTime;
import java.util.List;

import com.example.project.Files.FileMetadataDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseContentDTO {
    private Long id;

    @NotNull(message = "Course id must not be null")
    private Long courseId;

    @NotNull(message = "Instructor id must not be null")
    private Long instructorId; 

    @NotBlank(message = "Title must not be blank")
    private String title;
    
    private Long sectionId;
    private String sectionTitle;

    private LocalDateTime uploadDate;

    private List<FileMetadataDTO> files;

}
