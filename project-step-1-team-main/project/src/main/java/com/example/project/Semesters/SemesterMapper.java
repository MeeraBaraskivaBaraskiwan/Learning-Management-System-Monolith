package com.example.project.Semesters;

import org.springframework.stereotype.Component;

@Component
public class SemesterMapper {
    public SemesterDTO toDTO(Semester s) {
        if (s == null) return null;
        SemesterDTO dto = new SemesterDTO();
        dto.setId(s.getId());
        dto.setTerm(s.getTerm());
        dto.setYear(s.getYear());
        return dto;
    }
    public Semester toEntity(SemesterDTO dto) {
        if (dto == null) return null;
        return new Semester(dto.getTerm(), dto.getYear());
    }
}
