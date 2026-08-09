package com.example.project.Enrollments;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.project.Courses.Course;
import com.example.project.Progress.ProgressDTO;
import com.example.project.Sections.Section;
import com.example.project.Students.Student;

@Component
public class EnrollmentMapper {

    public EnrollmentDTO toDTO(Enrollment enrollment) {
        if (enrollment == null) return null;

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent() != null ? enrollment.getStudent().getId() : null);
        dto.setCourseId(enrollment.getCourse() != null ? enrollment.getCourse().getId() : null);
        dto.setSectionId(enrollment.getSection() != null ? enrollment.getSection().getId() : null);
        dto.setCompleted(enrollment.isCompleted());
        dto.setCurrentProgress(enrollment.getCurrentProgress());

        List<ProgressDTO> progressDTOs = enrollment.getProgressRecords().stream().map(progress -> {
            ProgressDTO progressDTO = new ProgressDTO();
            progressDTO.setId(progress.getId());
            progressDTO.setEnrollmentId(enrollment.getId());
            progressDTO.setCourseContentId(progress.getCourseContent() != null ? progress.getCourseContent().getId() : null);
            progressDTO.setProgress(progress.getProgress());
            progressDTO.setCompletedTasks(progress.getCompletedTasks());
            progressDTO.setTotalTasks(progress.getTotalTasks());
            progressDTO.setUpdatedAt(progress.getUpdatedAt());
            return progressDTO;
        }).collect(Collectors.toList());
        dto.setProgressRecords(progressDTOs);
        return dto;
    }
 

public Enrollment toEntity(EnrollmentDTO dto, Student student, Section section) {
        if (dto == null || student == null || section == null) return null;

        // use the new constructor that accepts student + section
        Enrollment enrollment = new Enrollment(student, section);
        enrollment.setCompleted(dto.isCompleted());
        enrollment.setCurrentProgress(dto.getCurrentProgress());
        return enrollment;
    }
}
