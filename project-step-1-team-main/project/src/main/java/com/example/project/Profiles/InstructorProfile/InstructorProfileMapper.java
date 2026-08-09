package com.example.project.Profiles.InstructorProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Instructors.Instructor;
import com.example.project.Profiles.Profile.Profile;

@Component
public class InstructorProfileMapper {

    private static final Logger logger = LoggerFactory.getLogger(InstructorProfileMapper.class);

    public InstructorProfileDTO toDTO(InstructorProfile instructorProfile) {
        if (instructorProfile == null) {
            logger.warn("Attempted to map a null InstructorProfile entity to DTO");
            return null;
        }

        logger.info("Mapping InstructorProfile entity with ID: {} to DTO", instructorProfile.getId());
        InstructorProfileDTO dto = new InstructorProfileDTO();
        dto.setId(instructorProfile.getId());
        dto.setProfileId(instructorProfile.getProfile() != null ? instructorProfile.getProfile().getId() : null);
        dto.setInstructorId(instructorProfile.getInstructor() != null ? instructorProfile.getInstructor().getId() : null);
        dto.setFaculty(instructorProfile.getFaculty());
        dto.setDepartment(instructorProfile.getDepartment());
        dto.setAcademicRank(instructorProfile.getAcademicRank());
        logger.info("Successfully mapped InstructorProfile entity with ID: {} to DTO", instructorProfile.getId());
        return dto;
    }

    public InstructorProfile toEntity(InstructorProfileDTO dto, Profile profile, Instructor instructor) {
        if (dto == null || profile == null || instructor == null) {
            logger.warn("Attempted to map a null InstructorProfileDTO, Profile, or Instructor to InstructorProfile entity");
            return null;
        }

        logger.info("Mapping InstructorProfileDTO with ID: {} to InstructorProfile entity", dto.getId());
        InstructorProfile entity = new InstructorProfile();
        entity.setProfile(profile);
        entity.setInstructor(instructor);
        entity.setFaculty(dto.getFaculty());
        entity.setDepartment(dto.getDepartment());
        entity.setAcademicRank(dto.getAcademicRank());
        logger.info("Successfully mapped InstructorProfileDTO with ID: {} to InstructorProfile entity", dto.getId());
        return entity;
    }

    public void updateEntity(InstructorProfile entity, InstructorProfileDTO dto, Profile profile, Instructor instructor) {
        if (entity == null || dto == null || profile == null || instructor == null) {
            logger.warn("Attempted to update a null InstructorProfile entity, DTO, Profile, or Instructor");
            return;
        }

        logger.info("Updating InstructorProfile entity with ID: {} using InstructorProfileDTO with ID: {}", entity.getId(), dto.getId());
        entity.setProfile(profile);
        entity.setInstructor(instructor);
        entity.setFaculty(dto.getFaculty());
        entity.setDepartment(dto.getDepartment());
        entity.setAcademicRank(dto.getAcademicRank());
        logger.info("Successfully updated InstructorProfile entity with ID: {}", entity.getId());
    }
}
