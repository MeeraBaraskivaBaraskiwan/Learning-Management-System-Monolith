package com.example.project.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: {}", token);
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Malformed JWT token: {}", token);
            throw e;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", token);
            throw e;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", token, e);
            throw e;
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token: {}", token, e);
            throw e;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (Exception e) {
            logger.error("Error extracting claims from token: {}", token, e);
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        logger.info("Generating JWT token for user: {}", userDetails.getUsername());
        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, userDetails.getUsername());
        logger.info("JWT token generated successfully for user: {}", userDetails.getUsername());
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        logger.debug("Creating JWT token for subject: {}", subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10*20))// 200 min
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (isValid) {
                logger.info("JWT token is valid for user: {}", username);
            } else {
                logger.warn("JWT token is invalid for user: {}", username);
            }
            return isValid;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token has expired: {}", token);
            return false;
        } catch (Exception e) {
            logger.error("Error validating JWT token: {}", token, e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            logger.info("JWT token has expired: {}", token);
        }
        return expired;
    }
}