package com.nineleaps.authentication.jwt.controllerexceptions;

public class UserSSOException extends RuntimeException {
    public UserSSOException(String message) {
        super(message);
    }
}