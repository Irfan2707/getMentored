package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {


    JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletResponse response) throws ConflictException;

    public User getUserViaPhoneNumber(String phoneNumber);

    String getEncodedPassword(String password);

    public User updateUser(User user);

    public User getUserByMail(String userMai);

    User getUserById(Long id);

    User registerNewUser(UserRegistrationDTO registrationDTO);


    ResponseEntity<JwtResponse> insertSsoUser(SSOUserDto newSsoUser);

    public User findUserByEmail(String userMail);


    Object authenticate(AuthenticationRequest loginRequest, HttpServletResponse response);
}