package com.example.project.Profiles.Profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.project.Users.User;

@Component
public class ProfileMapper {

    private static final Logger logger = LoggerFactory.getLogger(ProfileMapper.class);

    public ProfileDTO toDTO(Profile profile) {
        if (profile == null) {
            logger.warn("Attempted to map a null Profile entity to DTO");
            return null;
        }

        logger.info("Mapping Profile entity with ID: {} to DTO", profile.getId());
        ProfileDTO dto = new ProfileDTO();
        dto.setId(profile.getId());
        dto.setUserId(profile.getUser() != null ? profile.getUser().getId() : null);
        dto.setFullName(profile.getFullName());
        dto.setPhoneNumber(profile.getPhoneNumber());
        dto.setCountry(profile.getCountry());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        dto.setBio(profile.getBio());
        dto.setBirthDate(profile.getBirthDate());
        dto.setNationality(profile.getNationality());
        dto.setGender(profile.getGender());
        dto.setCreatedAt(profile.getCreatedAt());
        dto.setUpdatedAt(profile.getUpdatedAt());
        logger.info("Successfully mapped Profile entity with ID: {} to DTO", profile.getId());
        return dto;
    }

    public Profile toEntity(ProfileDTO dto, User user) {
        if (dto == null || user == null) {
            logger.warn("Attempted to map a null ProfileDTO or User to Profile entity");
            return null;
        }

        logger.info("Mapping ProfileDTO with ID: {} and User with ID: {} to Profile entity", dto.getId(), user.getId());
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setFullName(dto.getFullName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setCountry(dto.getCountry());
        profile.setProfilePictureUrl(dto.getProfilePictureUrl());
        profile.setBio(dto.getBio());
        profile.setBirthDate(dto.getBirthDate());
        profile.setNationality(dto.getNationality());
        profile.setGender(dto.getGender());
        logger.info("Successfully mapped ProfileDTO with ID: {} to Profile entity", dto.getId());
        return profile;
    }

    public void updateEntity(Profile profile, ProfileDTO dto) {
        if (profile == null || dto == null) {
            logger.warn("Attempted to update a null Profile entity or with a null ProfileDTO");
            return;
        }

        logger.info("Updating Profile entity with ID: {} using ProfileDTO with ID: {}", profile.getId(), dto.getId());
        profile.setFullName(dto.getFullName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setCountry(dto.getCountry());
        profile.setProfilePictureUrl(dto.getProfilePictureUrl());
        profile.setBio(dto.getBio());
        profile.setBirthDate(dto.getBirthDate());
        profile.setNationality(dto.getNationality());
        profile.setGender(dto.getGender());
        logger.info("Successfully updated Profile entity with ID: {}", profile.getId());
    }
}