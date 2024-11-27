package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.ForgotPasswordServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ForgotPasswordServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private ForgotPasswordServiceImpl emailService;
    @Mock
    private Logger logger;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGenerateAndSendOTPValidUser() {
        // Arrange
        String userEmail = "hadi@gmail.com";
        User user = new User();
        user.setUserMail(userEmail);
        when(userRepository.getUserEmail(userEmail)).thenReturn(user);

        // Act
        String result = emailService.generateAndSendOTP(userEmail);

        // Assert
        assertEquals("DONE ...", result);
        verify(userRepository, times(1)).getUserEmail(userEmail);
    }

    @Test
    void testGenerateAndSendOTPInvalidUser() {
        // Arrange
        String userEmail = "hadi@gmail.com";
        when(userRepository.getUserEmail(userEmail)).thenReturn(null);

        // Act
        String result = emailService.generateAndSendOTP(userEmail);

        // Assert
        assertEquals("You are not a valid user", result);
        verify(userRepository, times(1)).getUserEmail(userEmail);
    }

    @Test
    void testGenerateRandomOTP() {
        // Act
        String otp = emailService.generateRandomOTP();

        // Assert
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d+"));
    }


    @Test
    void testVerifyOTPValidOTP() {
        // Arrange
        String storedUserEmail = "hadi@gmail.com";
        String storedOTP = "123456";
        emailService.storeOTP(storedUserEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP("123456");

        // Assert
        assertTrue(result);
    }

    @Test
    void testVerifyCorrectOTP() {
        // Arrange
        String userEmail = "hadi@gmail.com";
        String storedOTP = "123456";
        long creationTime = System.currentTimeMillis();

        emailService.otpMap.put(userEmail, creationTime);
        emailService.otpMapForOtp.put(userEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP(storedOTP);

        // Assert
        assertTrue(result, "Correct OTP should return true");
    }

    @Test
    void testVerifyIncorrectOTP() {
        // Arrange
        String userEmail = "user@example.com";
        String storedOTP = "123456"; // The correct OTP is "1234567"

        emailService.otpMap.put(userEmail, System.currentTimeMillis());
        emailService.otpMapForOtp.put(userEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP("1234567"); // Incorrect OTP

        // Assert
        assertFalse(result, "Incorrect OTP should return false");
    }


    @Test
    void testVerifyExpiredOTP() {
        // Arrange

        String userEmail = "hadi@gmail.com";
        String storedOTP = "123456";
        long timeout = ForgotPasswordServiceImpl.TIMEOUT;
        long expiredTime = System.currentTimeMillis() - (timeout + 1000);

        emailService.otpMap.put(userEmail, expiredTime);
        emailService.otpMapForOtp.put(userEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP(storedOTP);

        // Assert
        assertFalse(result, "Expired OTP should return false");
    }
    @Test
    void testVerifyExpiredSession() {
        // Arrange
        String userEmail = "hadi@gmail.com";
        String storedOTP = "123456";
        long timeout = ForgotPasswordServiceImpl.TIMEOUT;
        long expiredTime = System.currentTimeMillis() - (timeout + 1000);

        emailService.otpMap.put(userEmail, expiredTime);
        emailService.otpMapForOtp.put(userEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP("123456");

        // Assert
        assertFalse(result, "Expired session with null creationTime should return false");
    }

    @Test
    void testVerifyExpiredSessions() {
        // Arrange
        String userEmail = "hadi@gmail.com";
        String storedOTP = "123456";

        // Set the creationTime to null
        emailService.otpMap.put(userEmail, null);
        emailService.otpMapForOtp.put(userEmail, storedOTP);

        // Act
        boolean result = emailService.verifyOTP("123456");

        // Assert
        assertFalse(result, "Expired session with null creationTime should return false");
    }




//    @Test
//    void testSendEmail() {
//        // Arrange
//        String userEmail = "hadi@gmail.com";
//        String otp = "123456";
//        emailService.otpMap.put(userEmail, System.currentTimeMillis());
//        emailService.otpMapForOtp.put(userEmail, otp);
//
//        // Act
//        boolean result = emailService.sendEmail(otp, userEmail);
//
//        // Assert
//        assertTrue(result, "Sending email should return true");
//    }



    @Test
    void testChangePassword_UserExists() {
        // Arrange
        String userEmail = "user@example.com";
        String newPassword = "newPassword";

        // Mock UserRepository to return a user
        User existingUser = new User();
        when(userRepository.getUserEmail(userEmail)).thenReturn(existingUser);

        // Mock BCryptPasswordEncoder
        when(bCryptPasswordEncoder.encode(newPassword)).thenReturn("hashedPassword");

        // Act
        ResponseEntity<String> result = emailService.changePassword(userEmail, newPassword);

        // Assert
        assertEquals("Password changed", result.getBody());
        verify(userRepository, times(1)).getUserEmail(userEmail);
        verify(bCryptPasswordEncoder, times(1)).encode(newPassword);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testChangePassword_UserDoesNotExist() {
        // Arrange
        String userEmail = "nonexistent@example.com";
        String newPassword = "newPassword";

        // Mock UserRepository to return null (user does not exist)
        when(userRepository.getUserEmail(userEmail)).thenReturn(null);

        // Act
        ResponseEntity<String> result = emailService.changePassword(userEmail, newPassword);

        // Assert
        assertEquals("You are not a valid user", result.getBody());
        verify(userRepository, times(1)).getUserEmail(userEmail);
        verify(bCryptPasswordEncoder, never()).encode(newPassword);
        verify(userRepository, never()).save(any(User.class));
    }


    }



