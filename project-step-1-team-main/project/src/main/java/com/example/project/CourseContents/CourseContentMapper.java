package com.example.project.CourseContents;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.project.Courses.Course;
import com.example.project.Files.FileMetadataMapper;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

@Component
public class CourseContentMapper {

    public CourseContentDTO toDTO(CourseContent courseContent) {
        if (courseContent == null) return null;

        CourseContentDTO dto = new CourseContentDTO();
        dto.setId(courseContent.getId());
        dto.setCourseId(courseContent.getCourse().getId());
        dto.setInstructorId(courseContent.getInstructor().getId());
        dto.setTitle(courseContent.getTitle());
        dto.setSectionId(courseContent.getSection().getId());
        dto.setSectionTitle("Section " +courseContent.getSection().getNumber());
        dto.setUploadDate(courseContent.getUploadDate());
        dto.setFiles(
            courseContent.getFileMetadataList().stream()
                .map(fm -> FileMetadataMapper.toDTO(fm))
                .collect(Collectors.toList())
        );
        return dto;
    }

    public CourseContent toEntity(CourseContentDTO dto, Course course, Instructor instructor,Section section) {
        if (dto == null || course == null || instructor == null) return null;

        return new CourseContent(course, instructor, dto.getTitle(),section);
    }
}
