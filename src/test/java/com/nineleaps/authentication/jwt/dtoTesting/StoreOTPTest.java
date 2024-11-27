package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.StoreOTP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class StoreOTPTest {



        @Test
        void testGetOtp() {
            StoreOTP.setOtp(1234);

            assertEquals(1234, StoreOTP.getOtp());
        }

        @Test
        void testSetOtp() {
            StoreOTP.setOtp(5678);

            assertEquals(5678, StoreOTP.getOtp());
        }
}
