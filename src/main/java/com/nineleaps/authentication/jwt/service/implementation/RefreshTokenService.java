package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.repository.RefreshTokenRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public RefreshToken createRefreshToken(String emailId) {
        try {
            RefreshToken refreshToken = new RefreshToken.RefreshTokenBuilder()
                    .user(userRepository.findByUserMail(emailId))
                    .token(UUID.randomUUID().toString())
                    .expiryDate(Instant.now().plusMillis(172800000)) // 2 days
                    .build();

            refreshToken = refreshTokenRepository.save(refreshToken);
            logger.info("Created refresh token for email: {}", emailId);
            return refreshToken;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating refresh token for email: " + emailId, e);
        }
    }


    public Optional<RefreshToken> findByToken(String token) {
        try {
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);
            if (refreshToken.isPresent()) {
                RefreshToken foundToken = refreshToken.get();
                logger.info("Found RefreshToken with token: {}", foundToken.getToken());
            } else {
                logger.info("RefreshToken not found for token: {}", token);
            }
            return refreshToken;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error finding refresh token by token: " + token, e);
        }
    }


    public Optional<RefreshToken> verifyExpiration(RefreshToken refreshToken) {
        try {
            if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(refreshToken);
                logger.info("Refresh token expired and deleted for user: {}", refreshToken.getUser().getUserMail());
                return Optional.empty();
            }
            return Optional.of(refreshToken);
        } catch (Exception e) {

            return Optional.empty();
        }

    }
}