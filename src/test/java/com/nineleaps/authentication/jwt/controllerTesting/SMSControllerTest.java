package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.SMSController;
import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.SmsPojo;
import com.nineleaps.authentication.jwt.dto.StoreOTP;
import com.nineleaps.authentication.jwt.dto.TempOTP;
import com.nineleaps.authentication.jwt.service.implementation.SmsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class SMSControllerTest {

    @Mock
    private SmsServiceImpl smsServiceImpl;

    @Mock
    private SimpMessagingTemplate webSocket;

    @InjectMocks
    private SMSController smsController;

    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSmsSubmitSuccess() {
        // Arrange
        SmsPojo sms = new SmsPojo();
        sms.setPhoneNumber("1234567890");

        when(smsServiceImpl.send(sms)).thenReturn(true);

        // Act
        ResponseEntity<Object> responseEntity = smsController.smsSubmit(sms);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("OTP sent successfully", responseBody.get("message"));


    }

    @Test
    void testSmsSubmitFailure() {
        // Arrange
        SmsPojo sms = new SmsPojo();
        sms.setPhoneNumber("1234567890");

        when(smsServiceImpl.send(sms)).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = smsController.smsSubmit(sms);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Failed to send OTP", responseBody.get("error"));


    }

    @Test
    void testVerifyOTPSignUpSuccess() {
        // Arrange
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(123456);

        StoreOTP.setOtp(123456);

        // Act
        ResponseEntity<Object> responseEntity = smsController.verifyOTPSignUp(tempOTP, null);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Correct OTP verified for sign up", responseBody.get("message"));

    }

    @Test
    void testVerifyOTPSignUpFailure() {
        // Arrange
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(123456);

        StoreOTP.setOtp(654321);

        // Act
        ResponseEntity<Object> responseEntity = smsController.verifyOTPSignUp(tempOTP, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Incorrect OTP provided for sign up", responseBody.get("error"));


    }

    @Test
    void testVerifyOTPSuccess() {
        // Arrange
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(123456);

        JwtResponse jwtResponse = new JwtResponse();
        when(smsServiceImpl.verifyOTPAndGenerateTokens(tempOTP)).thenReturn(jwtResponse);

        // Act
        ResponseEntity<Object> responseEntity = smsController.verifyOTP(tempOTP, null);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("OTP verification successful for login", responseBody.get("message"));


    }

    @Test
    void testVerifyOTPFailure() {
        // Arrange
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(123456);

        when(smsServiceImpl.verifyOTPAndGenerateTokens(tempOTP)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = smsController.verifyOTP(tempOTP, null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Failed to verify OTP for login.", responseBody.get("error"));


    }
}
