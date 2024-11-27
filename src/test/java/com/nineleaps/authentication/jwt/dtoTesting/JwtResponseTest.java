package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.JwtResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class JwtResponseTest {

    @Test
    void testNoArgsConstructor() {
        JwtResponse jwtResponse = new JwtResponse();

        assertNull(jwtResponse.getAccessToken());
        assertNull(jwtResponse.getRefreshToken());
        assertNull(jwtResponse.getErrorMessage());
    }

    @Test
    void testAllArgsConstructor() {
        String accessToken = "access_token_value";
        String refreshToken = "refresh_token_value";
        String errorMessage = "error_message_value";

        JwtResponse jwtResponse = new JwtResponse(
                accessToken,
                refreshToken,
                errorMessage
        );

        assertEquals(accessToken, jwtResponse.getAccessToken());
        assertEquals(refreshToken, jwtResponse.getRefreshToken());
        assertEquals(errorMessage, jwtResponse.getErrorMessage());
    }

    @Test
    void testGettersAndSetters() {
        JwtResponse jwtResponse = new JwtResponse();

        String accessToken = "access_token_value";
        String refreshToken = "refresh_token_value";
        String errorMessage = "error_message_value";

        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken);
        jwtResponse.setErrorMessage(errorMessage);

        assertEquals(accessToken, jwtResponse.getAccessToken());
        assertEquals(refreshToken, jwtResponse.getRefreshToken());
        assertEquals(errorMessage, jwtResponse.getErrorMessage());
    }
}
