package com.example.project.Security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.Users.User;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByExpiryDateBefore(Instant timestamp);

    @Modifying
    @Transactional
    void deleteByUser(User user);

     Optional<RefreshToken> findByUser(User user);
}