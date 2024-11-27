package com.nineleaps.authentication.jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DuplicateEngagementException extends RuntimeException {
    public DuplicateEngagementException(String message) {
        super(message);
    }
}
