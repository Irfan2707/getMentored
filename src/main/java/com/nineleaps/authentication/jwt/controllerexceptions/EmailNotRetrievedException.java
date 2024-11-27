package com.nineleaps.authentication.jwt.controllerexceptions;

public class EmailNotRetrievedException extends RuntimeException {
    public EmailNotRetrievedException(String menteeEmailNotFound) {
        super(menteeEmailNotFound);
    }
}
