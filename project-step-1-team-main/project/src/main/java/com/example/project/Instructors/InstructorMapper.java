package com.example.project.Instructors;

import org.springframework.stereotype.Component;

import com.example.project.Users.User;

@Component
public class InstructorMapper {

    public InstructorDTO toDTO(Instructor instructor) {
        if (instructor == null) return null;

        InstructorDTO dto = new InstructorDTO();
        dto.setId(instructor.getId());

        User user = instructor.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
        }
        dto.setInstructorId(instructor.getInstructorId());
        dto.setFaculty(instructor.getFaculty());
        dto.setDepartment(instructor.getDepartment());

        return dto;
    }

    public Instructor toEntity(InstructorDTO dto, User user) {
        if (dto == null || user == null) return null;

        return new Instructor(user, dto.getInstructorId(), dto.getFaculty(), dto.getDepartment());
    }
}
