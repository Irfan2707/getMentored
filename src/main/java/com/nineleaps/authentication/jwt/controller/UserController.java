package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.controllerexceptions.UserNotFoundException;
import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * Register New User in the Platform.
     *
     * @param registrationDTO The registration details of the new user.
     * @return A response entity indicating the result of the user registration process.
     */
    @PostMapping("/api/v1/signUp")
    @ApiOperation("Register New User in the Platform")
    public ResponseEntity<Object> registerNewUser(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
        User user = userServiceImpl.registerNewUser(registrationDTO);
        return ResponseHandler.success("User registered successfully", HttpStatus.CREATED, user);
    }

    /**
     * Register and Login using Single Sign On with Google.
     *
     * @param newSsoUser The details of the user registering and logging in using Single Sign On.
     * @param response   The HTTP servlet response.
     * @return A response entity indicating the result of the Single Sign On user registration and login process.
     */
    @PostMapping(value = "/api/v1/ssoUser", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Register and Login using Single Sign On with Google")
    public ResponseEntity<Object> insertSsoUser(@RequestBody @Valid SSOUserDto newSsoUser, HttpServletResponse response) {
        try {
            ResponseEntity<JwtResponse> jwtResponse = userServiceImpl.insertSsoUser(newSsoUser);
            return ResponseHandler.success("User registered and logged in successfully", HttpStatus.CREATED, jwtResponse.getBody());
        } catch (Exception ex) {
            return ResponseHandler.error("User registration and login failed: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get User Details By User Id.
     *
     * @param userId The ID of the user to retrieve details.
     * @return A response entity indicating the result of retrieving user details by user ID.
     * @throws UserNotFoundException if the user with the specified ID is not found.
     */
    @GetMapping("/getUserById")
    @ApiOperation("Get User Details By User Id")
    public ResponseEntity<Object> getById(@RequestParam Long userId) {
        User user = userServiceImpl.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found");

        }

        return ResponseHandler.success("User retrieved successfully", HttpStatus.OK, user);

    }


}


