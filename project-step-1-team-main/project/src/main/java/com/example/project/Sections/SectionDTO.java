package com.example.project.Sections;

import com.example.project.Semesters.SemesterDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SectionDTO {
    private Long id;

    @NotNull
    private Long courseId;

    @Min(1)
    private int number;

    private String schedule;

    @NotNull
    private Long semesterId;

    private SemesterDTO semester;
}
