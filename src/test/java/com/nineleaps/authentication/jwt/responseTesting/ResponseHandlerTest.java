package com.nineleaps.authentication.jwt.responseTesting;

import com.nineleaps.authentication.jwt.response.ResponseHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    @InjectMocks
    private ResponseHandler responseHandler;

   @Test
   void testSuccessResponse() {
       String message = "Success message";
       HttpStatus httpStatus = HttpStatus.OK;
       Object responseObject = "Response data";

       ResponseEntity<Object> responseEntity = ResponseHandler.success(message, httpStatus, responseObject);

       assertEquals(httpStatus, responseEntity.getStatusCode());
       assertEquals(message, ((Map) responseEntity.getBody()).get("message"));
       assertEquals(httpStatus, ((Map) responseEntity.getBody()).get("httpStatus"));
       assertEquals(responseObject, ((Map) responseEntity.getBody()).get("data"));
   }

   @Test
   void testErrorResponse() {
       String errorMessage = "Error message";
       HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

       ResponseEntity<Object> responseEntity = ResponseHandler.error(errorMessage, httpStatus);

       assertEquals(httpStatus, responseEntity.getStatusCode());
       assertEquals(errorMessage, ((Map) responseEntity.getBody()).get("error"));
       assertEquals(httpStatus, ((Map) responseEntity.getBody()).get("httpStatus"));
   }
}
