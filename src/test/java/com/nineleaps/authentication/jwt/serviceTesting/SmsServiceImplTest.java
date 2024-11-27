package com.nineleaps.authentication.jwt.serviceTesting;


import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.StoreOTP;
import com.nineleaps.authentication.jwt.dto.TempOTP;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.service.implementation.RefreshTokenService;
import com.nineleaps.authentication.jwt.service.implementation.SmsServiceImpl;
import com.nineleaps.authentication.jwt.service.interfaces.IUserService;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.rest.lookups.v2.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class SmsServiceImplTest {
   @Value("${twilio.accountSid}")
   private String accountSid;

   @Value("${twilio.authToken}")
   private String authToken;


    @Value("${jwt.secret}")
    private String secretKey;

    @InjectMocks
    private SmsServiceImpl smsServiceImpl;

    @Mock
    private IUserService iUserService;

    @Mock
    private RefreshTokenService refreshTokenService;
   @Mock
   private Logger logger;

   @Mock
   private MessageCreator messageCreator;

   @Autowired
   private SecureRandom secureRandom;

   @Mock
   private StoreOTP storeOTP;

   @Mock
   private PhoneNumber phoneNumber;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(smsServiceImpl, "secretKey", "mySecretKey");

        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(smsServiceImpl, "accountSid", accountSid);
        ReflectionTestUtils.setField(smsServiceImpl, "authToken", authToken);


    }

    @Test
     void testVerifyOTPAndGenerateTokens_InvalidOTP() {
        // Arrange
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(999999);

        // Act
        JwtResponse jwtResponse = smsServiceImpl.verifyOTPAndGenerateTokens(tempOTP);

        // Assert
        assertNotNull(jwtResponse);

        assertEquals("Invalid OTP", jwtResponse.getErrorMessage());

        assertNull(jwtResponse.getAccessToken());
        assertNull(jwtResponse.getRefreshToken());
    }


    @Test
     void testGenerateToken_ValidUser() {
        // Arrange
        String email = "test@example.com";
        String phoneNumber = "1234567890";

        User mockUser = new User();
        mockUser.setId(1L);
        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);
        mockUser.setRoles(roles);

        // Mock the behavior of userService to return the mockUser
        when(iUserService.getUserByMail(email.trim())).thenReturn(mockUser);

        // Act
        String token = smsServiceImpl.generateToken(email, phoneNumber);

        // Assert
        assertNotNull(token);

        assertFalse(token.isEmpty());


    }


   @Test
    void testSecretKey() {
      // Set up a test value for jwt.secret
      String testSecretKey = "testSecret";

      ReflectionTestUtils.setField(smsServiceImpl, "secretKey", testSecretKey);

      String result = smsServiceImpl.getSecretKey();

      assertEquals(testSecretKey, result);
   }

   @Test
    void testAccountSid() {
      // Set up a test value for twilio.accountSid
      String testAccountSid = "testAccountSid";

      ReflectionTestUtils.setField(smsServiceImpl, "accountSid", testAccountSid);

      String result = smsServiceImpl.getAccountSid();

      assertEquals(testAccountSid, result);
   }

   @Test
    void testAuthToken() {
      // Set up a test value for twilio.authToken
      String testAuthToken = "testAuthToken";

      ReflectionTestUtils.setField(smsServiceImpl, "authToken", testAuthToken);

      String result = smsServiceImpl.getAuthToken();

      assertEquals(testAuthToken, result);
   }

   @Test
    void testFromNumber() {
      // Set up a test value for twilio.fromNumber
      String testFromNumber = "+1234567890";

      ReflectionTestUtils.setField(smsServiceImpl, "fromNumber", testFromNumber);

      String result = smsServiceImpl.getFromNumber();

      assertEquals(testFromNumber, result);
   }

   @Test
   void testGetTimeStamp() {
      // Call the getTimeStamp() method
      String timeStamp = smsServiceImpl.getTimeStamp();

      assertNotNull(timeStamp);
   }

   @Test
   void testGetPhoneNumber() {
      String phoneNumber = "+1234567890";
      smsServiceImpl.setPhoneNumber(phoneNumber);

      // Call the getPhoneNumber() method
      String retrievedPhoneNumber = smsServiceImpl.getPhoneNumber();

      assertEquals(phoneNumber, retrievedPhoneNumber);
   }




}

