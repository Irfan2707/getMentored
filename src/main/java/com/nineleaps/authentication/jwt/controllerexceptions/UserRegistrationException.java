package com.nineleaps.authentication.jwt.controllerexceptions;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
}