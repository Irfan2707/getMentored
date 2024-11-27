package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.SmsPojo;
import com.nineleaps.authentication.jwt.dto.StoreOTP;
import com.nineleaps.authentication.jwt.dto.TempOTP;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.SmsServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequestMapping("/api/v1/OTP")
@RestController
@RequiredArgsConstructor
public class SMSController {

    @Value("${jwt.secret}")
    private String secretKey;

    private final SmsServiceImpl smsServiceImpl;


    private final SimpMessagingTemplate webSocket;
    private static String topicDestination = "/lesson/sms";
    private static final Logger logger = LoggerFactory.getLogger(SMSController.class);


    /**
     * Send an OTP for Login using phone Number and OTP.
     *
     * @param sms The SMS details containing the phone number to which the OTP will be sent.
     * @return A response entity indicating the result of the OTP sending process.
     */
    @PostMapping("/send")
    @ApiOperation("Send an OTP for Login using phone Number and OTP")
    public ResponseEntity<Object> smsSubmit(@RequestBody SmsPojo sms) {
        if (smsServiceImpl.send(sms)) {
            logger.info("OTP sent successfully for phone number: {}", sms.getPhoneNumber());
            webSocket.convertAndSend(topicDestination, smsServiceImpl.getTimeStamp() + ": SMS has been sent " + sms.getPhoneNumber());
            return ResponseHandler.success("OTP sent successfully", HttpStatus.OK, "otp sent");
        } else {
            logger.error("Failed to send OTP for phone number: {}", sms.getPhoneNumber());
            return ResponseHandler.error("Failed to send OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Verify the OTP sent to your Mobile Number for sign Up using phone Number and OTP.
     *
     * @param sms     The temporary OTP details for sign-up verification.
     * @param response The HTTP servlet response.
     * @return A response entity indicating the result of the OTP verification process for sign-up.
     */
    @PostMapping("/verifyOTP/signUp")
    @ApiOperation("Verify the OTP sent to your Mobile Number for sign Up using phone Number and OTP")
    public ResponseEntity<Object> verifyOTPSignUp(@RequestBody TempOTP sms, HttpServletResponse response) {
        if (sms.getOtp() == StoreOTP.getOtp()) {
            logger.info("Correct OTP verified for sign up");
            return ResponseHandler.success("Correct OTP verified for sign up", HttpStatus.OK, "correct otp");
        } else {
            logger.error("Incorrect OTP provided for sign up");
            return ResponseHandler.error("Incorrect OTP provided for sign up", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Verify the OTP sent to your Mobile Number for Login using phone Number and OTP.
     *
     * @param tempOTP The temporary OTP details for login verification.
     * @param response The HTTP servlet response.
     * @return A response entity indicating the result of the OTP verification process for login.
     */
    @PostMapping("/login/verifyOTP")
    @ApiOperation("Verify the OTP sent to your Mobile Number for Login using phone Number and OTP")
    public ResponseEntity<Object> verifyOTP(@RequestBody TempOTP tempOTP, HttpServletResponse response) {

        JwtResponse jwtResponse = smsServiceImpl.verifyOTPAndGenerateTokens(tempOTP);
        if (jwtResponse != null) {
            logger.info("OTP verification successful for login");
            return ResponseHandler.success("OTP verification successful for login", HttpStatus.OK, jwtResponse);
        } else {
            logger.error("Failed to verify OTP for login: ");
            return ResponseHandler.error("Failed to verify OTP for login.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
