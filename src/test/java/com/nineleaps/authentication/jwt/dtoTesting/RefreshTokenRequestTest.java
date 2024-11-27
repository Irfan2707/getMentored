package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class RefreshTokenRequestTest {

    @Test
    void testNoArgsConstructor() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();

        assertNull(refreshTokenRequest.getToken());
    }

    @Test
    void testAllArgsConstructor() {
        String token = "refreshToken123";
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(token);

        assertEquals(token, refreshTokenRequest.getToken());
    }

    @Test
    void testGettersAndSetters() {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();

        String token = "refreshToken456";
        refreshTokenRequest.setToken(token);

        assertEquals(token, refreshTokenRequest.getToken());
    }
}
