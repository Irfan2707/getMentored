package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.UserController;
import com.nineleaps.authentication.jwt.controllerexceptions.UserNotFoundException;
import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.service.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterNewUser() {
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUserMail("irfan@gmail.com");
        registrationDTO.setPassword("irfan");
        registrationDTO.setPhoneNumber("7889340372");
        User user = new User();
        user.setUserMail("irfan@gmail.com");
        user.setUserPassword("irfan");
        user.setPhoneNumber("7889340372");


        when(userServiceImpl.registerNewUser(registrationDTO)).thenReturn(user);

        ResponseEntity<Object> responseEntity = userController.registerNewUser(registrationDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        User responseData = (User) responseBody.get("data");
        assertNotNull(responseData);
        assertEquals("User registered successfully", responseBody.get("message"));
    }


    @Test
    void testInsertSsoUserSuccess() {
        // Arrange
        SSOUserDto newSsoUser = new SSOUserDto();
        ResponseEntity<JwtResponse> jwtResponseEntity = ResponseEntity.ok(new JwtResponse());

        when(userServiceImpl.insertSsoUser(newSsoUser)).thenReturn(jwtResponseEntity);

        // Act
        ResponseEntity<Object> responseEntity = userController.insertSsoUser(newSsoUser, null);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("User registered and logged in successfully", responseBody.get("message"));

        JwtResponse jwtResponse = (JwtResponse) responseBody.get("data");
        assertNotNull(jwtResponse);
    }

    @Test
    void testInsertSsoUserFailure() {
        // Arrange
        SSOUserDto newSsoUser = new SSOUserDto();
        String errorMessage = "Registration failed";

        when(userServiceImpl.insertSsoUser(newSsoUser)).thenThrow(new RuntimeException(errorMessage));

        // Act
        ResponseEntity<Object> responseEntity = userController.insertSsoUser(newSsoUser, null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("User registration and login failed: " + errorMessage, responseBody.get("error"));
    }

    @Test
    void testGetById() {
        Long userId = 1L;
        User user = new User();

        when(userServiceImpl.getUserById(userId)).thenReturn(user);

        ResponseEntity<Object> responseEntity = userController.getById(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

    }

    @Test
    void testGetUserByIdUserNotFound() {
        // Arrange
        Long userId = 1L;

        // Mock user not found
        when(userServiceImpl.getUserById(userId)).thenReturn(null);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userController.getById(userId));
    }


//    @Test
//    void testDeleteUserByIdSuccess() {
//        // Arrange
//        Long userId = 1L;
//
//        // Mock successful user deletion
//        doNothing().when(userServiceImpl).deleteUserById(userId);
//
//        // Act
//        ResponseEntity<Object> responseEntity = userController.deleteUserById(userId);
//
//        // Assert
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//
//        // Check the response body structure
//        assertTrue(responseEntity.getBody() instanceof Map);
//        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
//        assertEquals("User deleted successfully", responseBody.get("message"));
//
//        verify(userServiceImpl).deleteUserById(userId);
//    }
//
//    @Test
//    void testDeleteUserByIdFailure() {
//        // Arrange
//        Long userId = 1L;
//
//        // Mock user deletion failure
//        doThrow(UserDeletionException.class).when(userServiceImpl).deleteUserById(userId);
//
//        // Act
//        assertThrows(UserDeletionException.class, () -> userController.deleteUserById(userId));
//    }
}
