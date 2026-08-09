package com.example.project.Profiles.StudentProfile;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface StudentProfileService {
    ResponseEntity<EntityModel<StudentProfileDTO>> createStudentProfile(StudentProfileDTO dto);
    EntityModel<StudentProfileDTO> getStudentProfileById(Long id);
    EntityModel<StudentProfileDTO> getStudentProfileByStudentId(Long studentId);
    PagedModel<EntityModel<StudentProfileDTO>> getAllStudentProfiles(Pageable pageable);
    ResponseEntity<EntityModel<StudentProfileDTO>> updateStudentProfile(Long id, StudentProfileDTO dto);
    ResponseEntity<?> deleteStudentProfile(Long id);
}