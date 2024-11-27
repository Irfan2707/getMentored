package com.nineleaps.authentication.jwt.utilTesting;


import com.auth0.jwt.interfaces.DecodedJWT;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JwtUtilsTest {
    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private JwtParser jwtParser;



    @Mock
    private Jws<Claims> claimsJws;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUtils.setSecretKey("HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");


    }

    @Mock
    private Claims claims;



    @Test
    void testSetSecretKey() {

        String newSecretKey = "new-secret-key";
        String result = jwtUtils.setSecretKey(newSecretKey);
        assertEquals(newSecretKey, result);
    }


    @Test
    void testExtractEmail() {

        ReflectionTestUtils.setField(jwtUtils, "secretKey", "HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGFuaWZ1QGdtYWlsLmNvbSIsInJvbGVzIjoiTUVOVEVFIiwiZXhwIjoxNzAzMzk1NTUzLCJ1c2VySWQiOjQxfQ.OkzEZuVvFwajjeTMaXiJADfk1dM4hduVn73XR1JgGfo";
        String email = jwtUtils.extractEmail(token);
        assertEquals("elanifu@gmail.com", email);
    }

    @Test
    void testExtractSsoUserIdentifier() {
        // Arrange
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGFuaWZ1QGdtYWlsLmNvbSIsInJvbGVzIjoiTUVOVEVFIiwiZXhwIjoxNzAzMzk1NTUzLCJ1c2VySWQiOjQxfQ.OkzEZuVvFwajjeTMaXiJADfk1dM4hduVn73XR1JgGfo";
        String userIdentifier = jwtUtils.extractSsoUserIdentifier(token);
        assertEquals("elanifu@gmail.com", userIdentifier);
    }


    @Test
    void testGetUserMailFromToken() {
        // Arrange
        ReflectionTestUtils.setField(jwtUtils, "secretKey", "HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGFuaWZ1QGdtYWlsLmNvbSIsInJvbGVzIjoiTUVOVEVFIiwiZXhwIjoxNzAzMzk1NTUzLCJ1c2VySWQiOjQxfQ.OkzEZuVvFwajjeTMaXiJADfk1dM4hduVn73XR1JgGfo";

        // Mock the behavior of the jwtParser and claims
        when(jwtParser.parseClaimsJws(token)).thenReturn(mock(Jws.class));
        Claims claims = Jwts.claims();
        claims.setSubject("elanifu@gmail.com");
        when(jwtParser.parseClaimsJws(token).getBody()).thenReturn(claims);

        String email = jwtUtils.getUserMailFromToken(token);

        assertEquals("elanifu@gmail.com", email);
    }
//
//    @Test
//    void testExtractPhoneNumber() {
//        // Arrange
//        ReflectionTestUtils.setField(jwtUtils, "secretKey", "HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");
//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGFuaWZ1QGdtYWlsLmNvbSIsInJvbGVzIjoiTUVOVEVFIiwiZXhwIjoxNzAzMzk1NTUzLCJ1c2VySWQiOjQxfQ.OkzEZuVvFwajjeTMaXiJADfk1dM4hduVn73XR1JgGfo";
//
//        // Mock the behavior of the jwtParser and claims
//        when(jwtParser.parseClaimsJws(token)).thenReturn(mock(Jws.class));
//        Claims claims = Jwts.claims();
//        claims.put("phoneNumber", "test-phone-number");
//        when(jwtParser.parseClaimsJws(token).getBody()).thenReturn(claims);
//
//        String phoneNumber = jwtUtils.extractPhoneNumber(token);
//
//        assertEquals("+91 9025756850", phoneNumber);
//    }




    @Test
    void testIsTokenExpired() {
        // Arrange
        Date expiration = new Date(System.currentTimeMillis() - 1000L); // Past date
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getExpiresAt()).thenReturn(expiration);

        // Act
        boolean result = jwtUtils.isTokenExpired(decodedJWT);

        // Assert
        assertTrue(result);
    }

    @Test
    void testIsTokensExpired() {
        // Arrange
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(decodedJWT.getExpiresAt()).thenReturn(new Date(System.currentTimeMillis() + 1000L)); // Expires 1 second in the future
        boolean result = jwtUtils.isTokenExpired(decodedJWT);
        assertFalse(result);
    }

    @Test
    void testGetExpirationDateFromToken() {

        ReflectionTestUtils.setField(jwtUtils, "secretKey", "HRlELXqpSBqqeeq4tq66u6wrtqarevwatq4rlilolsertstw4v434y4w");
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGFuaWZ1QGdtYWlsLmNvbSIsInJvbGVzIjoiTUVOVEVFIiwiZXhwIjoxNzAzMzk1NTUzLCJ1c2VySWQiOjQxfQ.OkzEZuVvFwajjeTMaXiJADfk1dM4hduVn73XR1JgGfo";
        Date actualExpirationDate = new Date(System.currentTimeMillis() + 1000L * 60L * 60L * 24L * 30L);
        when(jwtParser.parseClaimsJws(token)).thenReturn(claimsJws);
        Claims claims = Jwts.claims();
        claims.setExpiration(actualExpirationDate);
        when(claimsJws.getBody()).thenReturn(claims);
        Date result = jwtUtils.getExpirationDateFromToken(token);
        assertNotEquals(actualExpirationDate, result);
    }



}
