package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;



class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);
    }


@Test
void testSendEmail_Success() {
    // Arrange
    String subject = "Test Subject";
    String message = "Test Message";
    String to = "elanifu@gmail.com";

    // Act
    boolean result = emailService.sendEmail(subject, message, to);

    // Assert
    assertTrue(result);
}

    @Test
     void testSendEmail_NullRecipient() {
        // Arrange
        String subject = "Test Subject";
        String message = "Test Message";
        String to = null;

        // Act
        boolean result = emailService.sendEmail(subject, message, to);

        // Assert
        assertFalse(result);

    }

}

