package com.example.project.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.Map;

@Tag(name = "Current User", description = "Operations related to the currently authenticated user")
@RestController
@RequestMapping("/user")
public class CurrentUserController {

    private static final Logger logger = LoggerFactory.getLogger(CurrentUserController.class);

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get current user", description = "Retrieve details of the currently authenticated user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User details retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    @GetMapping
    public Object getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (principal instanceof UserDetails userDetails) {
            logger.info("Authenticated user via Form Login: {}", userDetails.getUsername());
            return Map.of(
                "username", userDetails.getUsername(),
                "authType", "Form Login"
            );
        } else if (principal instanceof OidcUser oidcUser) {
            logger.info("Authenticated user via Google OAuth2: {}", oidcUser.getEmail());
            return Map.of(
                "username", oidcUser.getEmail(),
                "authType", "Google OAuth2"
            );
        }
        logger.warn("Unauthenticated access attempt to /user endpoint");
        return Map.of("message", "User not authenticated");
    }
}

