package com.nineleaps.authentication.jwt.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static java.lang.System.getProperties;

@Service
public class EmailServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public boolean sendEmail(String subject, String message, String to) {
        if (to == null) {
            // Handle the null value here, such as throwing an exception or returning false
            logger.error("Recipient email address is null. Email not sent.");
            return false;
        }
        // Variable for gmail
        String host = "smtp.gmail.com";

        boolean f = false;
        // get the system properties
        Properties properties = getProperties();
        // setting important information to properties object
        // host set
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("hema.b@nineleaps.com", "nnvipaqlddkjoguw");
            }
        });
        session.setDebug(true);
        // Step 2 : compose the message [text,multi media]
        MimeMessage m = new MimeMessage(session);
        try {
            // from email
            m.setFrom();
            // adding recipient to message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // adding subject to message
            m.setSubject(subject);
            // adding text to message
            m.setText(message);
            // send
            // Step 3 : send the message using Transport class
            Transport.send(m);

            f = true;
        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", to, e.getMessage());

        }
        return f;
    }
}
