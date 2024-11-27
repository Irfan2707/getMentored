package com.nineleaps.authentication.jwt.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private ResponseHandler() {
//        No args constructor
    }


    public static ResponseEntity<Object> success(String message, HttpStatus httpStatus, Object responseObject) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("httpStatus", httpStatus);
        response.put("data", responseObject);

        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<Object> error(String errorMessage, HttpStatus httpStatus) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);
        errorResponse.put("httpStatus", httpStatus);

        return new ResponseEntity<>(errorResponse, httpStatus);
    }


}