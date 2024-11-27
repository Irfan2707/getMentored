package com.nineleaps.authentication.jwt.controller;


import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.interfaces.IEmailServices;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@AllArgsConstructor
public class ForgotPasswordController {


    private String otp;

    private final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);


    private IEmailServices emailServicesImplementation;

    @Autowired
    public ForgotPasswordController(IEmailServices emailServicesImplementation) {
        this.emailServicesImplementation = emailServicesImplementation;
    }


    /**
     * Endpoint for generating and sending an OTP to the registered Email ID.
     *
     * @param userEmail The Email ID for which to generate and send the OTP.
     * @return A response entity indicating the result of the OTP generation and sending process.
     */
    @PostMapping("/api/v1/sendOtp")
    @ApiOperation("Receive an OTP for your registered Email Id")
    public ResponseEntity<Object> generateAndSendOTP(@RequestParam String userEmail) {
        // Generate and send OTP to the registered EmailId
        String resultMessage = emailServicesImplementation.generateAndSendOTP(userEmail);

        if (resultMessage == null) {

            logger.error("Failed to send OTP to user: {}", userEmail);

            return ResponseHandler.error("Failed to send OTP.", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            logger.info("OTP sent successfully to user: {}", userEmail);

            return ResponseHandler.success("OTP sent successfully.", HttpStatus.OK, resultMessage);
        }
    }


    /**
     * Endpoint for verifying the OTP sent for the registered Email ID.
     *
     * @param userEnteredOtp The OTP entered by the user for verification.
     * @return A response entity indicating whether the OTP verification was successful or not.
     */
    @PostMapping("/api/v1/verifyOtp")
    @ApiOperation("Verify the OTP sent for the registered Email Id")
    public ResponseEntity<Object> verifyOTP(@RequestParam String userEnteredOtp) {
        boolean isOTPVerified = emailServicesImplementation.verifyOTP(userEnteredOtp);

        if (isOTPVerified) {
            logger.info("OTP verified successfully.");

            return ResponseHandler.success("OTP verified successfully.", HttpStatus.OK, null);
        } else {
            logger.error("OTP verification failed.");

            return ResponseHandler.error("OTP verification failed.", HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Endpoint for changing the password for a user profile by entering a new password.
     *
     * @param newpass The new password to be set for the user.
     * @param email   The Email ID of the user whose password is to be changed.
     * @return A response entity indicating the result of the password change process.
     */
    @PutMapping("/api/v1/changePassword")
    @ApiOperation("Change the password for your profile by entering a New Password")
    public ResponseEntity<Object> changePassword(@RequestParam("newpass") String newpass, @RequestParam("email") String email) {
        ResponseEntity<String> result = emailServicesImplementation.changePassword(email, newpass);

        if (result.getStatusCode() == HttpStatus.OK) {
            logger.info("Password changed successfully for email: {}", email);
            return ResponseHandler.success("Password changed successfully.", HttpStatus.OK, result);
        } else {
            logger.error("Failed to change password for email: {}", email);
            return ResponseHandler.error("Failed to change password.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}