package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RefreshTokenTest {

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken();
    }

    @Test
    void testGettersAndSetters() {
        int id = 1;
        String token = "testToken";
        Instant expiryDate = Instant.now();
        User user = new User();

        refreshToken.setId(id);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setUser(user);

        assertEquals(id, refreshToken.getId());
        assertEquals(token, refreshToken.getToken());
        assertEquals(expiryDate, refreshToken.getExpiryDate());
        assertEquals(user, refreshToken.getUser());
    }

    @Test
    void testBuilder() {
        int id = 1;
        String token = "testToken";
        Instant expiryDate = Instant.now();
        User user = new User();

        RefreshToken refreshToken = RefreshToken.builder()
                .id(id)
                .token(token)
                .expiryDate(expiryDate)
                .user(user)
                .build();

        assertEquals(id, refreshToken.getId());
        assertEquals(token, refreshToken.getToken());
        assertEquals(expiryDate, refreshToken.getExpiryDate());
        assertEquals(user, refreshToken.getUser());
    }

    @Test
    void testDefaultConstructor() {
        RefreshToken refreshToken = new RefreshToken();
        assertNull(refreshToken.getToken());
        assertNull(refreshToken.getExpiryDate());
        assertNull(refreshToken.getUser());
    }
}
