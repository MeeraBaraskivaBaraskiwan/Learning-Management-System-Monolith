package com.example.project.Assessments.Assessments_Assessment;

import com.example.project.Courses.Course;
import com.example.project.Instructors.Instructor;
import com.example.project.Sections.Section;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AssessmentMapper {

    private static final Logger logger = LoggerFactory.getLogger(AssessmentMapper.class);

    public static AssessmentDTO toDTO(Assessment assessment) {
        if (assessment == null) {
            logger.warn("Attempted to map a null Assessment to AssessmentDTO");
            return null;
        }

        logger.info("Mapping Assessment to AssessmentDTO: {}", assessment.getId());

        AssessmentDTO dto = new AssessmentDTO();
        dto.setId(assessment.getId());
        dto.setCourseCode(assessment.getCourse().getCode());
        dto.setInstructorId(assessment.getInstructor().getId());
         dto.setSectionId(assessment.getSection().getId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setType(assessment.getType());
        dto.setCreatedAt(assessment.getCreatedAt());
        dto.setDueDate(assessment.getDueDate());

        logger.debug("Mapped AssessmentDTO: {}", dto);
        return dto;
    }

    public static Assessment toEntity(AssessmentDTO dto, Course course, Instructor instructor,Section section) {
        if (dto == null) {
            logger.warn("Attempted to map a null AssessmentDTO to Assessment");
            return null;
        }
        if (course == null) {
            logger.error("Course is null while mapping AssessmentDTO to Assessment");
            return null;
        }
        if (instructor == null) {
            logger.error("Instructor is null while mapping AssessmentDTO to Assessment");
            return null;
        }

        logger.info("Mapping AssessmentDTO to Assessment: {}", dto.getId());

        Assessment assessment = new Assessment();
        assessment.setId(dto.getId());
         assessment.setSection(section);
        assessment.setTitle(dto.getTitle());
        assessment.setDescription(dto.getDescription());
        assessment.setType(dto.getType());
        assessment.setDueDate(dto.getDueDate());
        assessment.setCourse(course);
        assessment.setInstructor(instructor);

        logger.debug("Mapped Assessment: {}", assessment);
        return assessment;
    }
}

