package com.nineleaps.authentication.jwt.serviceTesting;
import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.repository.RefreshTokenRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateRefreshToken() {
        // Arrange
        String emailId = "test@example.com";
        User user = new User();
        user.setId(1L);
        user.setUserMail(emailId);

        RefreshToken expectedRefreshToken = new RefreshToken.RefreshTokenBuilder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(172800000)) // 2 days
                .build();

        // Mock the behavior of userRepository
        when(userRepository.findByUserMail(emailId)).thenReturn(user);

        // Mock the behavior of refreshTokenRepository
        when(refreshTokenRepository.save(any())).thenReturn(expectedRefreshToken);

        // Act
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(emailId);

        // Assert
        assertNotNull(refreshToken);
        assertEquals(expectedRefreshToken.getToken(), refreshToken.getToken());
        assertEquals(expectedRefreshToken.getUser(), refreshToken.getUser());
        assertEquals(expectedRefreshToken.getExpiryDate(), refreshToken.getExpiryDate());
    }


    @Test
    void testCreateRefreshTokenfailed() {
       // Arrange
       String emailId = "test@example.com";

       // Mock the behavior of userRepository
       User user = new User();
       Mockito.when(userRepository.findByUserMail(emailId)).thenReturn(user);

       // Mock the behavior of refreshTokenRepository to throw a DataAccessException
       doThrow(new DataAccessException("Test exception message") {
       }).when(refreshTokenRepository).save(Mockito.any());

       // Act and Assert
       Exception exception = assertThrows(IllegalArgumentException.class, () -> {
          refreshTokenService.createRefreshToken(emailId);
       });

       String expectedErrorMessage = "Error creating refresh token for email: " + emailId;
       String actualErrorMessage = exception.getMessage();
       assert(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    void testFindByTokenTokenFound() {
       // Arrange
       String token = "sampleToken";
       RefreshToken expectedRefreshToken = new RefreshToken();
       expectedRefreshToken.setToken(token);

       // Mock the behavior of refreshTokenRepository
       when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.of(expectedRefreshToken));

       // Act
       Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(token);

       // Assert
       assertTrue(refreshToken.isPresent());
       assertEquals(token, refreshToken.get().getToken());

    }

    @Test
    void testFindByTokenTokenNotFound() {
       // Arrange
       String token = "nonExistentToken";

       // Mock the behavior of refreshTokenRepository to return an empty Optional
       when(refreshTokenRepository.findByToken(token)).thenReturn(Optional.empty());

       // Act
       Optional<RefreshToken> refreshToken = refreshTokenService.findByToken(token);

       // Assert
       assertFalse(refreshToken.isPresent());


    }

    @Test
    void testFindByTokenException() {
       // Arrange
       String token = "exceptionToken";

       // Mock the behavior of refreshTokenRepository to throw a DataAccessException
       doThrow(new DataAccessException("Test exception message") {
       }).when(refreshTokenRepository).findByToken(token);

       // Act and Assert
       Exception exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> {
          refreshTokenService.findByToken(token);
       });

       String expectedErrorMessage = "Error finding refresh token by token: " + token;
       String actualErrorMessage = exception.getMessage();
       assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }


    @Test
     void testVerifyExpirationTokenNotExpired() {
        // Arrange
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(3600000)); // Expires in 1 hour

        // Act
        Optional<RefreshToken> result = refreshTokenService.verifyExpiration(refreshToken);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(refreshToken, result.get());
        verify(refreshTokenRepository, never()).delete(refreshToken);
    }

//


    @Test
    void testVerifyExpiration_TokenNotExpired() {
       RefreshToken refreshToken = new RefreshToken();
       refreshToken.setExpiryDate(Instant.now().plusMillis(1000)); // Future time
       Optional<RefreshToken> result = refreshTokenService.verifyExpiration(refreshToken);
       assertTrue(result.isPresent());
       assertEquals(refreshToken, result.get());
    }

//


    @Test
    void testVerifyExpiration_ExceptionInCatchBlock() {
       // Arrange
       RefreshToken refreshToken = new RefreshToken();
       refreshToken.setExpiryDate(Instant.now().minusSeconds(3600)); // Expiry 1 hour ago
       when(refreshTokenRepository.save(refreshToken)).thenThrow(new RuntimeException("Test exception"));

       // Act
       Optional<RefreshToken> result = refreshTokenService.verifyExpiration(refreshToken);

       // Assert
       assertFalse(result.isPresent());
    }


 }

