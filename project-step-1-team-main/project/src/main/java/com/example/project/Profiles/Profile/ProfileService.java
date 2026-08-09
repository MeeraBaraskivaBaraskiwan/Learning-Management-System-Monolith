package com.example.project.Profiles.Profile;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    ResponseEntity<EntityModel<ProfileDTO>> createProfile(ProfileDTO dto, Long userId);
    EntityModel<ProfileDTO> getProfileById(Long id);
    EntityModel<ProfileDTO> getProfileByUserId(Long userId);
    PagedModel<EntityModel<ProfileDTO>> getAllProfiles(Pageable pageable);
    ResponseEntity<EntityModel<ProfileDTO>> updateProfile(Long id, ProfileDTO dto, Long userId);
    ResponseEntity<?> deleteProfile(Long id);
}