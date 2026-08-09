package com.example.project.Courses;

import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) return null;

        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());

        return dto;
    }

    public Course toEntity(CourseDTO dto) {
        if (dto == null) return null;

        return new Course(dto.getName(), dto.getCode(), dto.getDescription(), dto.getCredits());
    }
}
