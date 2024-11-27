package com.nineleaps.authentication.jwt.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StoreOTP {

    private static int otp = 0;

    private StoreOTP() {

    }

    public static int getOtp() {
        return otp;
    }

    public static void setOtp(int otp) {
        StoreOTP.otp = otp;
    }
}
