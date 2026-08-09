package com.example.project.Progress;

import org.springframework.stereotype.Component;

@Component
public class ProgressMapper {

    public ProgressDTO toDTO(Progress progress) {
        if (progress == null) return null;

        ProgressDTO dto = new ProgressDTO();
        dto.setId(progress.getId());
        dto.setEnrollmentId(progress.getEnrollment().getId());
        dto.setProgress(progress.getProgress());
        dto.setUpdatedAt(progress.getUpdatedAt());

        if (progress.getCourseContent() != null) {
            dto.setCourseContentId(progress.getCourseContent().getId());
        }

        return dto;
    }

    public Progress toEntity(ProgressDTO dto, Progress progress) {
        if (dto == null || progress == null) return null;

        progress.setProgress(dto.getProgress());
        return progress;
    }
}
