package com.nineleaps.authentication.jwt.service.implementation;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.SmsPojo;
import com.nineleaps.authentication.jwt.dto.StoreOTP;
import com.nineleaps.authentication.jwt.dto.TempOTP;
import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.service.interfaces.IUserService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.lang.System.currentTimeMillis;

@Component
@Getter
@Setter
public class SmsServiceImpl {

    @Value("${jwt.secret}")
    private String secretKey;
    @Autowired
    IUserService userService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImpl.class);


    private String phoneNumber;


    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.fromNumber}")
    private String fromNumber;


    public boolean send(SmsPojo sms) {
        logger.info("Sending SMS");
        Twilio.init(accountSid, authToken);
        logger.info("Twilio initialized");

        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(900000) + 100000; // Generates a random 6-digit number

        String msg = "Your OTP - " + number + "   please verify this OTP!";
        Message.creator(new PhoneNumber(sms.getPhoneNumber()), new PhoneNumber(fromNumber), msg)
                .create();
        StoreOTP.setOtp(number);
        phoneNumber = sms.getPhoneNumber();
        logger.info("SMS sent to phone number: {}", phoneNumber);
        return true;
    }


    public JwtResponse verifyOTPAndGenerateTokens(TempOTP tempOTP) {
        if (tempOTP.getOtp() == StoreOTP.getOtp()) {
            String verifiedPhoneNumber = getPhoneNumber(); // Rename the local variable
            User user = userService.getUserViaPhoneNumber(verifiedPhoneNumber);
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUserMail());
            String email = user.getUserMail();
            String accessToken = generateToken(email, verifiedPhoneNumber);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(accessToken);
            jwtResponse.setRefreshToken(refreshToken.getToken());
            return jwtResponse;
        } else {
            // Return an error response for invalid OTP
            JwtResponse errorResponse = new JwtResponse();
            errorResponse.setErrorMessage("Invalid OTP");
            return errorResponse;
        }
    }

    public String generateToken(String email, String phoneNumber) {
        User userDetails = userService.getUserByMail(email.trim());
        Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());
        Set<UserRole> roles = userDetails.getRoles();
        // Extract role names as strings
        List<String> roleNames = new ArrayList<>();
        for (UserRole role : roles) {
            roleNames.add(role.toString());
        }
        // Convert the List<String> to a comma-separated string
        String rolesString = String.join(",", roleNames);
        return JWT.create()
                .withSubject(email)
                .withClaim("phoneNumber", phoneNumber) // Add phone number claim
                .withClaim("roles", rolesString)
                .withClaim("userId", userDetails.getId())
                // .withExpiresAt(new Date(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000))
                .withExpiresAt(new Date(currentTimeMillis() + 1000L * 60L * 60L * 24L * 30L))
                .sign(algorithm);
    }

    public String getTimeStamp() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}