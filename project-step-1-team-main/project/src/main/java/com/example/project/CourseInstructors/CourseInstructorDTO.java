package com.example.project.CourseInstructors;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseInstructorDTO {
  private Long id;

  @NotNull Long instructorId;
  @NotNull Long courseId;

  /** now only the section FK */
  @NotNull(message="Section id is required")
  private Long sectionId;
}

