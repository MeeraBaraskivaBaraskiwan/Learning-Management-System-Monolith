package com.example.project.Notifications.UserPrefrence;

import com.example.project.Users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPreferenceMapper {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceMapper.class);

    public static UserPreferenceDTO toDTO(UserPreference preference) {
        if (preference == null) {
            logger.warn("Attempted to map a null UserPreference entity to DTO");
            return null;
        }

        logger.info("Mapping UserPreference entity with ID: {} to DTO", preference.getId());
        UserPreferenceDTO dto = new UserPreferenceDTO();
        dto.setId(preference.getId());
        dto.setUserId(preference.getUser().getId());
        dto.setEmailEnabled(preference.isEmailEnabled());
        dto.setSmsEnabled(preference.isSmsEnabled());
        dto.setPhoneNumber(preference.getPhoneNumber());
        logger.info("Successfully mapped UserPreference entity with ID: {} to DTO", preference.getId());
        return dto;
    }

    public static UserPreference toEntity(UserPreferenceDTO dto, User user) {
        if (dto == null || user == null) {
            logger.warn("Attempted to map a null UserPreferenceDTO or User to UserPreference entity");
            return null;
        }

        logger.info("Mapping UserPreferenceDTO with ID: {} to UserPreference entity", dto.getId());
        UserPreference preference = new UserPreference();
        preference.setUser(user);
        preference.setEmailEnabled(dto.isEmailEnabled());
        preference.setSmsEnabled(dto.isSmsEnabled());
        preference.setPhoneNumber(dto.getPhoneNumber());
        logger.info("Successfully mapped UserPreferenceDTO with ID: {} to UserPreference entity", dto.getId());
        return preference;
    }
}
