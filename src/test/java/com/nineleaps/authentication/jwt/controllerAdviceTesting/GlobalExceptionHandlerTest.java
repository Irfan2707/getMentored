package com.nineleaps.authentication.jwt.controllerAdviceTesting;

import com.nineleaps.authentication.jwt.controlleradvice.GlobalExceptionHandler;
import com.nineleaps.authentication.jwt.controllerexceptions.*;
import com.nineleaps.authentication.jwt.exception.DuplicateConnectionRequestException;
import com.nineleaps.authentication.jwt.exception.MappingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleUserRegistrationException() {
        UserRegistrationException ex = new UserRegistrationException("User registration failed");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleUserRegistrationException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleUserSSOException() {
        UserSSOException ex = new UserSSOException("User SSO failed");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleUserSSOException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleUserDeletionException() {
        UserDeletionException ex = new UserDeletionException("User deletion failed");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleUserDeletionException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void handleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleResourceNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleUserNotFoundException() {
        // Test the UserNotFoundException handler
        UserNotFoundException ex = new UserNotFoundException("User not found");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleUserNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleEngagementNotFoundException() {
        // Test the EngagementNotFoundException handler
        EngagementNotFoundException ex = new EngagementNotFoundException("Engagement not found");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleEngagementNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleIllegalArgumentException() {
        // Test the IllegalArgumentException handler
        IllegalArgumentException ex = new IllegalArgumentException("Invalid argument");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    void handleConnectionRequestNotFoundException() {
        ConnectionRequestNotFoundException ex = new ConnectionRequestNotFoundException("Connection request not found");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleConnectionRequestNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleDuplicateConnectionRequestException() {
        DuplicateConnectionRequestException ex = new DuplicateConnectionRequestException("Duplicate connection request");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleDupDuplicateConnectionRequestException(ex);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    void handleEmailNotRetrievedException() {
        EmailNotRetrievedException ex = new EmailNotRetrievedException("Email not retrieved");
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleEmailNotRetrievedException(ex);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testResourceNotFoundExceptionWithMessageAndCause() {
        Exception cause = new Exception("Root cause");
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found", cause);
        assertEquals("Resource not found", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }


    @Test
    void testMappingExceptionConstructor() {
        String errorMessage = "Mapping exception message";

        MappingException mappingException = new MappingException(errorMessage);

        assertEquals(errorMessage, mappingException.getMessage());

    }

    @Test
    void testConstructor() {
        String message = "Test Exception Message";
        NullPointerException cause = new NullPointerException("Test Null Pointer Exception");

        ConnectionRequestProcessingException exception = new ConnectionRequestProcessingException(message);


        assertEquals(message, exception.getMessage());


    }

}
