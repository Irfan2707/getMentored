package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.TempOTP;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class TempOTPTest {

    @Test
    void testGetOtp() {
        int otpValue = 123456;
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(otpValue);

        assertEquals(otpValue, tempOTP.getOtp());
    }

    @Test
    void testSetOtp() {
        int otpValue = 654321;
        TempOTP tempOTP = new TempOTP();
        tempOTP.setOtp(otpValue);

        assertEquals(otpValue, tempOTP.getOtp());
    }
}
