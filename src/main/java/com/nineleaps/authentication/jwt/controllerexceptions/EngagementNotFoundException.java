package com.nineleaps.authentication.jwt.controllerexceptions;

public class EngagementNotFoundException extends RuntimeException {
    public EngagementNotFoundException(String message) {
        super(message);
    }
}
