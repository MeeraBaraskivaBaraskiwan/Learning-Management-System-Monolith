package com.example.project.Notifications.UserPrefrence;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;



public interface UserPreferenceService {

    ResponseEntity<EntityModel<UserPreferenceDTO>> createPreference(UserPreferenceDTO dto);

    EntityModel<UserPreferenceDTO> getPreferenceById(Long id);

    EntityModel<UserPreferenceDTO> getPreferenceByUserId(Long userId);

    PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferences(Pageable pageable);

    PagedModel<EntityModel<UserPreferenceDTO>> getAllPreferencesNoArgs();
    
    ResponseEntity<EntityModel<UserPreferenceDTO>> updatePreference(Long id, UserPreferenceDTO dto);
    
    ResponseEntity<?> deletePreference(Long id);
}