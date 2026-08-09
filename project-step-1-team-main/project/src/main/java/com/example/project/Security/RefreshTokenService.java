package com.example.project.Security;

import com.example.project.Exceptions.InvalidRefreshTokenException;
import com.example.project.Exceptions.ResourceNotFoundException;
import com.example.project.Users.User;
import com.example.project.Users.UserRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

@Transactional
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ See if we already have one for this user:
        Optional<RefreshToken> existingOpt = refreshTokenRepository.findByUser(user);
        if (existingOpt.isPresent()) {
            RefreshToken existing = existingOpt.get();
            existing.setToken(UUID.randomUUID().toString());
            existing.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60)); // 7 days
            logger.info("Updating existing refresh token for user {}", user.getEmail());
            return refreshTokenRepository.save(existing);
        }

        // 2️⃣ Otherwise create a fresh row:
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiryDate(Instant.now().plusSeconds(7 * 24 * 60 * 60)); // 7 days
        logger.info("Creating new refresh token for user {}", user.getEmail());
        return refreshTokenRepository.save(rt);
    }


    public void debugAllTokens() {
        List<RefreshToken> tokens = refreshTokenRepository.findAll();
        logger.debug("Listing all refresh tokens in the database:");
        for (RefreshToken rt : tokens) {
            logger.debug("Token: {}, Expires: {}", rt.getToken(), rt.getExpiryDate());
        }
    }

    public RefreshToken verifyRefreshToken(String token) {
        logger.info("Verifying refresh token: {}", token);

        RefreshToken rt = refreshTokenRepository.findByToken(token)
                .filter(r -> r.getExpiryDate().isAfter(Instant.now()))
                .orElseThrow(() -> {
                    logger.warn("Token not found or expired: {}", token);
                    return new InvalidRefreshTokenException("Invalid or expired refresh token");
                });

        if (rt.getUser() == null) {
            logger.error("Token has no associated user: {}", token);
            throw new InvalidRefreshTokenException("Refresh token has no associated user");
        }

        logger.info("Token verified for user: {}", rt.getUser().getEmail());
        return rt;
    }

    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        try {
            logger.info("Rotating refresh token for user: {}", oldToken.getUser().getEmail());

            User user = oldToken.getUser();
            user.setRefreshToken(null);
            userRepository.save(user);
            logger.info("Cleared refresh token reference in user entity for user: {}", user.getEmail());

            refreshTokenRepository.deleteByUser(user);
            refreshTokenRepository.flush();
            logger.info("Old token deleted and flushed for user: {}", user.getEmail());

            RefreshToken newToken = createRefreshToken(user.getId());
            logger.info("New token created: {}", newToken.getToken());

            return newToken;

        } catch (Exception e) {
            logger.error("Exception in rotateRefreshToken: {}", e.getMessage(), e);
            throw e;
        }
    }

     @Transactional
    public void deleteByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
        deleteByUser(user);
    }
    
    @Transactional
public void deleteByUser(User user) {
    refreshTokenRepository.deleteByUser(user);
}
}