package com.example.project.Security;

import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public OAuth2AuthenticationSuccessHandler(
            JwtUtil jwtUtil,
            UserRepository userRepository,
            RefreshTokenService refreshTokenService
    ) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        try {
            String email;

            if (principal instanceof OidcUser oidcUser) {
                email = oidcUser.getAttribute("email");
                logger.info("OAuth2 authentication successful for OIDC user with email: {}", email);
                handleSuccessForOAuth2User(response, email, oidcUser.getAuthorities());
            } else if (principal instanceof OAuth2User oauth2User) {
                email = (String) oauth2User.getAttributes().get("email");
                logger.info("OAuth2 authentication successful for OAuth2 user with email: {}", email);
                handleSuccessForOAuth2User(response, email, oauth2User.getAuthorities());
            } else {
                logger.warn("Unsupported principal type: {}", principal.getClass().getName());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                String errorJson = objectMapper.writeValueAsString(
                    Map.of("error", "Unsupported principal type",
                           "principalClass", principal.getClass().getName())
                );
                response.getWriter().write(errorJson);
            }

        } catch (Exception ex) {
            logger.error("Error during OAuth2 authentication success handling: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            String errorJson = objectMapper.writeValueAsString(
                Map.of("error", "Failed to generate token", "message", ex.getMessage())
            );
            response.getWriter().write(errorJson);
            response.getWriter().flush();
        }
    }

    private void handleSuccessForOAuth2User(
            HttpServletResponse response,
            String email,
            java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> authorities
    ) throws IOException {

        logger.info("Handling successful authentication for user with email: {}", email);

        String accessToken = jwtUtil.generateToken(
            new org.springframework.security.core.userdetails.User(email, "", authorities)
        );
        logger.info("Generated access token for user with email: {}", email);

        User userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                logger.error("User not found in database with email: {}", email);
                return new ResourceNotFoundException("User not found with email: " + email);
            });

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userEntity.getId());
        logger.info("Generated refresh token for user with email: {}", email);

        respondWithTokens(response, accessToken, refreshToken.getToken());
    }

    private void respondWithTokens(HttpServletResponse response, String accessToken, String refreshToken)
            throws IOException {

        logger.info("Responding with tokens for user. Access token: {}, Refresh token: {}", accessToken, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Authorization", "Bearer " + accessToken);

        String jsonResponse = objectMapper.writeValueAsString(
            Map.of("accessToken", accessToken, "refreshToken", refreshToken)
        );
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}