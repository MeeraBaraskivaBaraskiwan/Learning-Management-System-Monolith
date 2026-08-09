package com.example.project.Security;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.Role;
import com.example.project.Users.RoleRepository;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOidcUserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        logger.info("Loading user information from Google OAuth2");

        OidcUserService delegate = new OidcUserService();
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String email = oidcUser.getAttribute("email");
        logger.info("Retrieved email from Google OAuth2: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email);

        String googleFirstName = oidcUser.getAttribute("given_name");
        String googleLastName = oidcUser.getAttribute("family_name");

        if (googleFirstName == null) {
            googleFirstName = "";
        }
        if (googleLastName == null) {
            googleLastName = "";
        }

        if (optionalUser.isEmpty()) {
            logger.info("No existing user found with email: {}. Creating a new user.", email);
            Role defaultRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> {
                    logger.error("Default role ROLE_STUDENT not found");
                    return new ResourceNotFoundException("Default role ROLE_STUDENT not found");
                });

            User newUser = new User(email, googleFirstName, googleLastName, "", defaultRole);
            userRepository.save(newUser);
            logger.info("New user created with email: {}", email);
        } else {
            User existingUser = optionalUser.get();
            boolean updated = false;

            if (!existingUser.getFirstName().equals(googleFirstName)) {
                logger.info("Updating first name for user with email: {}", email);
                existingUser.setFirstName(googleFirstName);
                updated = true;
            }
            if (!existingUser.getLastName().equals(googleLastName)) {
                logger.info("Updating last name for user with email: {}", email);
                existingUser.setLastName(googleLastName);
                updated = true;
            }
            if (updated) {
                userRepository.save(existingUser);
                logger.info("User with email: {} updated successfully", email);
            } else {
                logger.info("No updates required for user with email: {}", email);
            }
        }

        return oidcUser;
    }
}
