package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class UserRegistrationDTOTest {

    @Test
    void testGettersAndSetters() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

        String userMail = "hadi@gmail.com";
        String password = "password123";
        String phoneNumber = "1234567890";

        userRegistrationDTO.setUserMail(userMail);
        userRegistrationDTO.setPassword(password);
        userRegistrationDTO.setPhoneNumber(phoneNumber);

        assertEquals(userMail, userRegistrationDTO.getUserMail());
        assertEquals(password, userRegistrationDTO.getPassword());
        assertEquals(phoneNumber, userRegistrationDTO.getPhoneNumber());
    }
}
