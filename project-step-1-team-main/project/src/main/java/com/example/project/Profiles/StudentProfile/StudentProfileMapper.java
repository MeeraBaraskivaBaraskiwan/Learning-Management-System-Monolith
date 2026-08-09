package com.example.project.Profiles.StudentProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Profiles.InstructorProfile.InstructorProfile;
import com.example.project.Profiles.Profile.Profile;
import com.example.project.Students.Student;

@Component
public class StudentProfileMapper {

    private static final Logger logger = LoggerFactory.getLogger(StudentProfileMapper.class);

    public StudentProfileDTO toDTO(StudentProfile studentProfile) {
        if (studentProfile == null) {
            logger.warn("Attempted to map a null StudentProfile entity to DTO");
            return null;
        }

        logger.info("Mapping StudentProfile entity with ID: {} to DTO", studentProfile.getId());
        StudentProfileDTO dto = new StudentProfileDTO();
        dto.setId(studentProfile.getId());
        dto.setProfileId(studentProfile.getProfile() != null ? studentProfile.getProfile().getId() : null);
        dto.setStudentId(studentProfile.getStudent() != null ? studentProfile.getStudent().getId() : null);
        dto.setAdmittedYear(studentProfile.getAdmittedYear());
        dto.setMajor(studentProfile.getMajor());
        dto.setMinor(studentProfile.getMinor());
        dto.setCurrentSemester(studentProfile.getCurrentSemester());
        dto.setDegreeProgram(studentProfile.getDegreeProgram());
        dto.setStudentLevel(studentProfile.getStudentLevel());
        dto.setTawjihiStream(studentProfile.getTawjihiStream());
        dto.setProbationStatus(studentProfile.getProbationStatus());
        dto.setDepartment(studentProfile.getDepartment());
        dto.setAdvisorId(studentProfile.getAdvisor() != null ? studentProfile.getAdvisor().getId() : null);
        logger.info("Successfully mapped StudentProfile entity with ID: {} to DTO", studentProfile.getId());
        return dto;
    }

    public StudentProfile toEntity(StudentProfileDTO dto, Profile profile, InstructorProfile advisor, Student student) {
        if (dto == null || profile == null || advisor == null || student == null) {
            logger.warn("Attempted to map a null StudentProfileDTO, Profile, InstructorProfile, or Student to StudentProfile entity");
            return null;
        }

        logger.info("Mapping StudentProfileDTO with ID: {} to StudentProfile entity", dto.getId());
        StudentProfile entity = new StudentProfile();
        entity.setProfile(profile);
        entity.setStudent(student);
        entity.setAdmittedYear(dto.getAdmittedYear());
        entity.setMajor(dto.getMajor());
        entity.setMinor(dto.getMinor());
        entity.setCurrentSemester(dto.getCurrentSemester());
        entity.setDegreeProgram(dto.getDegreeProgram());
        entity.setStudentLevel(dto.getStudentLevel());
        entity.setTawjihiStream(dto.getTawjihiStream());
        entity.setProbationStatus(dto.getProbationStatus());
        entity.setDepartment(dto.getDepartment());
        entity.setAdvisor(advisor);
        logger.info("Successfully mapped StudentProfileDTO with ID: {} to StudentProfile entity", dto.getId());
        return entity;
    }

    public void updateEntity(StudentProfile entity, StudentProfileDTO dto, Profile profile, InstructorProfile advisor, Student student) {
        if (entity == null || dto == null || profile == null || advisor == null || student == null) {
            logger.warn("Attempted to update a null StudentProfile entity, DTO, Profile, InstructorProfile, or Student");
            return;
        }

        logger.info("Updating StudentProfile entity with ID: {} using StudentProfileDTO with ID: {}", entity.getId(), dto.getId());
        entity.setProfile(profile);
        entity.setStudent(student);
        entity.setAdmittedYear(dto.getAdmittedYear());
        entity.setMajor(dto.getMajor());
        entity.setMinor(dto.getMinor());
        entity.setCurrentSemester(dto.getCurrentSemester());
        entity.setDegreeProgram(dto.getDegreeProgram());
        entity.setStudentLevel(dto.getStudentLevel());
        entity.setTawjihiStream(dto.getTawjihiStream());
        entity.setProbationStatus(dto.getProbationStatus());
        entity.setDepartment(dto.getDepartment());
        entity.setAdvisor(advisor);
        logger.info("Successfully updated StudentProfile entity with ID: {}", entity.getId());
    }
}