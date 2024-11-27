package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.LoginController;
import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.RefreshTokenService;
import com.nineleaps.authentication.jwt.service.interfaces.IUserService;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private IUserService userService;

    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        // Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUserMail("user@example.com");
        loginRequest.setUserPassword("password");

        JwtResponse jwtResponse = new JwtResponse("accessToken", "refreshToken", "user@example.com");

        when(userService.authenticate(eq(loginRequest), any(HttpServletResponse.class))).thenReturn(ResponseEntity.ok(jwtResponse));

        // Act
        ResponseEntity<Object> responseEntity = loginController.authenticate(loginRequest, mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("User authenticated successfully.", responseBody.get("message"));
        JwtResponse response = (JwtResponse) responseBody.get("data");
        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());


        verify(logger, never()).error(anyString());
    }

    @Test
    void testAuthenticateFailure() {
        // Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest();
        loginRequest.setUserMail("user@example.com");
        loginRequest.setUserPassword("password");

        when(userService.authenticate(eq(loginRequest), any(HttpServletResponse.class))).thenReturn(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null));

        // Act
        ResponseEntity<Object> responseEntity = loginController.authenticate(loginRequest, mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Authentication failed.", responseBody.get("error"));


    }


    @Test
    void testRefreshTokenSuccess() throws ConflictException {
        // Arrange
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("validRefreshToken");

        JwtResponse jwtResponse = new JwtResponse("newAccessToken", "newRefreshToken", "user@example.com");

        when(userService.refreshToken(eq(refreshTokenRequest), any(HttpServletResponse.class))).thenReturn(jwtResponse);


        // Act
        ResponseEntity<Object> responseEntity = loginController.refreshToken(refreshTokenRequest, mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("JWT token refreshed successfully.", responseBody.get("message"));
        JwtResponse response = (JwtResponse) responseBody.get("data");
        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("newRefreshToken", response.getRefreshToken());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testRefreshTokenFailure() throws ConflictException {
        // Arrange
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken(null);

        when(userService.refreshToken(eq(refreshTokenRequest), any(HttpServletResponse.class))).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = loginController.refreshToken(refreshTokenRequest, mock(HttpServletResponse.class));

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(null, responseBody.get("data"));
        assertEquals("Failed to refresh JWT token.", responseBody.get("error"));


    }

    @Test
    void testDependencyInjection() {
        assertNotNull(loginController.getAuthenticationManager());
        assertNotNull(loginController.getUserDetailsService());
        assertNotNull(loginController.getJwtUtils());
        assertNotNull(loginController.getUserService());
        assertNotNull(loginController.getRefreshTokenService());
    }

}