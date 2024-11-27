package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthenticationRequestTest {

    @Test
    void testAllArgsConstructor() {
        String userMail = "test@example.com";
        String userPassword = "password123";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(userMail, userPassword);

        assertEquals(userMail, authenticationRequest.getUserMail());
        assertEquals(userPassword, authenticationRequest.getUserPassword());
    }

    @Test
    void testNoArgsConstructor() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();

        assertNull(authenticationRequest.getUserMail());
        assertNull(authenticationRequest.getUserPassword());
    }

    @Test
    void testGetterAndSetter() {
        String userMail = "test@example.com";
        String userPassword = "password123";

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUserMail(userMail);
        authenticationRequest.setUserPassword(userPassword);

        assertEquals(userMail, authenticationRequest.getUserMail());
        assertEquals(userPassword, authenticationRequest.getUserPassword());
    }







}
