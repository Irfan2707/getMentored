package com.nineleaps.authentication.jwt.service.interfaces;


import org.springframework.http.ResponseEntity;

public interface IEmailServices {

    public boolean sendEmail(String otp, String to);

    String generateAndSendOTP(String userEmail);


    boolean verifyOTP(String userEnteredotp);

    ResponseEntity<String> changePassword(String email, String newpass);
}