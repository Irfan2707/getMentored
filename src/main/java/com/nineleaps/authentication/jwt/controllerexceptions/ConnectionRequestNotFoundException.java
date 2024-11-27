package com.nineleaps.authentication.jwt.controllerexceptions;

public class ConnectionRequestNotFoundException extends RuntimeException {
    public ConnectionRequestNotFoundException(String connectionRequestNotFound) {
        super(connectionRequestNotFound);
    }
}
