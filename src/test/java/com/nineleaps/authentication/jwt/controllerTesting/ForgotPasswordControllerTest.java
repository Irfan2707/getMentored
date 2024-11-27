package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.ForgotPasswordController;
import com.nineleaps.authentication.jwt.service.implementation.ForgotPasswordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class ForgotPasswordControllerTest {

    @Mock
    private ForgotPasswordServiceImpl forgotPasswordServiceImpl;

    @Mock
    private Logger logger;
    @InjectMocks
    private ForgotPasswordController forgotPasswordController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGenerateAndSendOTPSuccess() {
        // Arrange
        String userEmail = "user@example.com";
        String otp = "123456";

        when(forgotPasswordServiceImpl.generateAndSendOTP(userEmail)).thenReturn(otp);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.generateAndSendOTP(userEmail);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("OTP sent successfully.", responseBody.get("message"));
        assertEquals(otp, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGenerateAndSendOTPFailure() {
        // Arrange
        String userEmail = "user@example.com";

        when(forgotPasswordServiceImpl.generateAndSendOTP(userEmail)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.generateAndSendOTP(userEmail);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to send OTP.", responseBody.get("error"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testVerifyOTPSuccess() {
        // Arrange
        String userEnteredOtp = "123456";

        when(forgotPasswordServiceImpl.verifyOTP(userEnteredOtp)).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.verifyOTP(userEnteredOtp);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("OTP verified successfully.", responseBody.get("message"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testVerifyOTPFailure() {
        // Arrange
        String userEnteredOtp = "123456";
        when(forgotPasswordServiceImpl.verifyOTP(userEnteredOtp)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.verifyOTP(userEnteredOtp);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("OTP verification failed.", responseBody.get("error"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testChangePasswordSuccess() {
        // Arrange
        String newpass = "newPassword";
        String email = "user@example.com";
        String successMessage = "Password changed successfully.";
        ResponseEntity<String> successResponse = ResponseEntity.ok(successMessage);

        when(forgotPasswordServiceImpl.changePassword(email, newpass))
                .thenReturn(successResponse);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.changePassword(newpass, email);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);


        assertEquals(successMessage, responseBody.get("message"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testChangePasswordFailure() {
        // Arrange
        String newpass = "newPassword";
        String email = "user@gmail.com";
        String errorMessage = "Failed to change password.";
        ResponseEntity<String> errorResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);

        when(forgotPasswordServiceImpl.changePassword(email, newpass))
                .thenReturn(errorResponse);

        // Act
        ResponseEntity<Object> responseEntity = forgotPasswordController.changePassword(newpass, email);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);


        assertEquals(errorMessage, responseBody.get("error"));

        verify(logger, never()).error(anyString());
    }


}