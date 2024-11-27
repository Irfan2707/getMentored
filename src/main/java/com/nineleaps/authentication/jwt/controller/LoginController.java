package com.nineleaps.authentication.jwt.controller;


import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.RefreshTokenService;
import com.nineleaps.authentication.jwt.service.interfaces.IUserService;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@Getter
@Setter
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1")
public class LoginController {


    private final AuthenticationManager authenticationManager;

    private final JwtUserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    private final IUserService userService;

    private final RefreshTokenService refreshTokenService;

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * Endpoint to authenticate a user by logging into the application using email and password.
     *
     * @param loginRequest The request containing user credentials (email and password).
     * @param response     The HttpServletResponse to handle the response.
     * @return A response entity indicating the result of the authentication process.
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ApiOperation("Login into the Application using Email and Password")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest loginRequest, HttpServletResponse response) {
        ResponseEntity<JwtResponse> responseEntity = (ResponseEntity<JwtResponse>) userService.authenticate(loginRequest, response);

        JwtResponse jwtResponse = responseEntity.getBody();

        if (jwtResponse == null) {
            logger.error("Authentication failed for user: {}", loginRequest.getUserMail());
            return ResponseHandler.error("Authentication failed.", HttpStatus.UNAUTHORIZED);
        } else {
            logger.info("User authenticated successfully: {}", loginRequest.getUserMail());
            return ResponseHandler.success("User authenticated successfully.", HttpStatus.OK, jwtResponse);
        }
    }


    /**
     * Endpoint to refresh a JWT token when it expires.
     *
     * @param refreshTokenRequest The request containing the refresh token.
     * @param response            The HttpServletResponse to handle the response.
     * @return A response entity indicating the result of the token refresh process.
     * @throws ConflictException If there is a conflict during token refresh.
     */
    @PostMapping("/refreshToken")
    @ApiOperation(value = "Api to refresh jwt token when it expires")
    public ResponseEntity<Object> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletResponse response) throws ConflictException {
        JwtResponse jwtResponse = userService.refreshToken(refreshTokenRequest, response);

        if (jwtResponse == null) {
            logger.error("Failed to refresh JWT token for user: ");
            return ResponseHandler.error("Failed to refresh JWT token.", HttpStatus.UNAUTHORIZED);

        } else {
            logger.info("JWT token refreshed successfully for user: ");
            return ResponseHandler.success("JWT token refreshed successfully.", HttpStatus.OK, jwtResponse);

        }
    }


}