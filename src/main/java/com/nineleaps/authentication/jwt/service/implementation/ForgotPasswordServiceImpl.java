package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IEmailServices;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.*;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperties;


@Service
//@RequiredArgsConstructor
@AllArgsConstructor

public class ForgotPasswordServiceImpl implements IEmailServices {

    private final UserRepository userRepository;
    private static final int OTP_LENGTH = 6;
    public static final int TIMEOUT = 5 * 60 * 1000; // 5 minutes in milliseconds
    private static String userEmail;
    public final Map<String, Long> otpMap = new HashMap<>();
    public final Map<String, String> otpMapForOtp = new HashMap<>();
    private final SecureRandom random = new SecureRandom();
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(ForgotPasswordServiceImpl.class);
//    private final Session session; // or a mail service interface


    @Override
    public String generateAndSendOTP(String userEmail) {
        logger.info("Generating and sending OTP for user email: {}", userEmail);

        User user = userRepository.getUserEmail(userEmail);

        if (user != null) {
            String otp = generateRandomOTP();
            storeOTP(userEmail, otp);
            scheduleTimeout(userEmail);
            sendEmail(otp, userEmail);
            logger.info("OTP generated and sent successfully for user email: {}", userEmail);
            return "DONE ...";
        } else {
            logger.warn("Invalid user email: {}", userEmail);
            return "You are not a valid user";
        }
    }

    public String generateRandomOTP() {
        StringBuilder otpBuilder = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otpBuilder.append(random.nextInt(10));
        }
        return otpBuilder.toString();
    }

    public void storeOTP(String userEmail, String otp) {
        otpMap.put(userEmail, currentTimeMillis());
        otpMapForOtp.put(userEmail, otp);
        // store OTP in database or cache
        logger.info("OTP generated and stored for user: {}", userEmail);
    }

    public void scheduleTimeout(String userEmail) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Invalidate the OTP here
                otpMap.remove(userEmail);
                // update OTP status in database or cache
                logger.info("OTP expired for user: {}", userEmail);
            }
        }, TIMEOUT);
    }


    @Override
    public boolean verifyOTP(String enteredOTP) {
        for (Map.Entry<String, String> entry : otpMapForOtp.entrySet()) {
            String storedUserEmail = entry.getKey();
            String storedOTP = entry.getValue();

            Long creationTime = otpMap.get(storedUserEmail);

            if (creationTime == null) {
                logger.warn("OTP session expired for user: {}", storedUserEmail);
            } else if (currentTimeMillis() - creationTime > TIMEOUT) {
                otpMap.remove(storedUserEmail);
                otpMapForOtp.remove(storedUserEmail);
                logger.warn("OTP has expired for user: {}", storedUserEmail);
            } else if (enteredOTP.equals(storedOTP)) {
                logger.info("OTP verified successfully for user: {}", storedUserEmail);
                return true; // OTP is verified
            }
        }

        logger.warn("OTP verification failed");
        return false; // OTP verification failed for all stored OTPs
    }


    // this is responsible to send email..
    @Override
    public boolean sendEmail(String otp, String to) {
        // Variable for Gmail
        String host = "smtp.gmail.com";
        String from = "learnbuddy";

        String subject = "Email Verification Through OTP";
        String message = "OTP for your verification is: ";
        boolean isSuccess = false;

        // Get the system properties
        Properties properties = getProperties();
        logger.info("Email service properties: {}", properties);

        // Setting important information to properties object

        // Host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Step 1: Get the session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("elanifu@gmail.com", "anyuvqzjhlwavwrd");
            }
        });

        session.setDebug(true);

        // Step 2: Compose the message [text, multimedia]
        MimeMessage mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setFrom(from);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message + otp);
            Transport.send(mimeMessage);
            isSuccess = true;
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", to, e.getMessage());
        }

        return isSuccess;
    }

    @Override
    public ResponseEntity<String> changePassword(String email, String newPassword) {
        User user = userRepository.getUserEmail(email);
        if (user != null) {
            user.setUserPassword(this.bCryptPasswordEncoder.encode(newPassword));
            this.userRepository.save(user);
            logger.info("Password changed for user with email: {}", email);
            return ResponseEntity.ok("Password changed");
        } else {
            logger.warn("Password change failed - User with email {} not found", email);
            return ResponseEntity.ok("You are not a valid user");
        }
    }


}