package com.example.project.Users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static final Logger logger = LoggerFactory.getLogger(UserMapper.class);

    public UserDTO toDTO(User user) {
        if (user == null) {
            logger.warn("Attempted to map a null User to UserDTO");
            return null;
        }

        logger.info("Mapping User to UserDTO with ID: {}", user.getId());
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword(user.getPassword());
        if (user.getRole() != null) {
            dto.setRoleName(user.getRole().getName());
        }
        return dto;
    }

    public User toEntity(UserDTO dto, Role role) {
        if (dto == null || role == null) {
            logger.warn("Attempted to map a null UserDTO or Role to User");
            return null;
        }

        logger.info("Mapping UserDTO to User with email: {}", dto.getEmail());
        return new User(
            dto.getEmail(),
            dto.getFirstName(),
            dto.getLastName(),
            dto.getPassword(),
            role
        );
    }
}
