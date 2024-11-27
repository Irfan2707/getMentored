package com.nineleaps.authentication.jwt.controlleradvice;

import com.nineleaps.authentication.jwt.controllerexceptions.*;
import com.nineleaps.authentication.jwt.exception.DuplicateConnectionRequestException;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<Object> handleUserRegistrationException(UserRegistrationException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserSSOException.class)
    public ResponseEntity<Object> handleUserSSOException(UserSSOException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserDeletionException.class)
    public ResponseEntity<Object> handleUserDeletionException(UserDeletionException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    //Handling Exceptions for feedback
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EngagementNotFoundException.class)
    public ResponseEntity<Object> handleEngagementNotFoundException(EngagementNotFoundException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConnectionRequestNotFoundException.class)
    public ResponseEntity<Object> handleConnectionRequestNotFoundException(ConnectionRequestNotFoundException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(DuplicateConnectionRequestException.class)
    public ResponseEntity<Object> handleDupDuplicateConnectionRequestException(DuplicateConnectionRequestException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailNotRetrievedException.class)
    public ResponseEntity<Object> handleEmailNotRetrievedException(EmailNotRetrievedException ex) {
        return ResponseHandler.error(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<String> handleNoteNotFoundException(NoteNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

