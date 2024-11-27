package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.SmsPojo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class SmsPojoTest {

    @Test
    void testNoArgsConstructor() {
        SmsPojo smsPojo = new SmsPojo();

        assertNull(smsPojo.getPhoneNumber());
    }

    @Test
    void testGettersAndSetters() {
        SmsPojo smsPojo = new SmsPojo();

        String phoneNumber = "1234567890";
        smsPojo.setPhoneNumber(phoneNumber);

        assertEquals(phoneNumber, smsPojo.getPhoneNumber());
    }
}
