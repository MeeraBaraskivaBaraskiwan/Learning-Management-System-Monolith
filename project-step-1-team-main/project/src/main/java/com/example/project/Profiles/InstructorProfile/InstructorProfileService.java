package com.example.project.Profiles.InstructorProfile;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface InstructorProfileService {
    ResponseEntity<EntityModel<InstructorProfileDTO>> createInstructorProfile(InstructorProfileDTO dto);
    EntityModel<InstructorProfileDTO> getInstructorProfileById(Long id);
    EntityModel<InstructorProfileDTO> getInstructorProfileByInstructorId(Long instructorId);
    PagedModel<EntityModel<InstructorProfileDTO>> getAllInstructorProfiles(Pageable pageable);
    ResponseEntity<EntityModel<InstructorProfileDTO>> updateInstructorProfile(Long id, InstructorProfileDTO dto, Authentication authentication);
    ResponseEntity<?> deleteInstructorProfile(Long id);
}