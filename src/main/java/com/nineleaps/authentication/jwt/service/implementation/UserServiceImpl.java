package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IUserService;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;

@Transactional
@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    public ResponseEntity<?> authenticate(AuthenticationRequest loginRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserMail(), loginRequest.getUserPassword()));
        } catch (BadCredentialsException ex) {
            logger.error("Authentication failed for userMail: {}", loginRequest.getUserMail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        User user = findUserByEmail(loginRequest.getUserMail());
        if (user == null) {
            logger.warn("User not found during authentication: {}", loginRequest.getUserMail());
            return ResponseEntity.notFound().build();
        }
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(loginRequest.getUserMail());
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserMail());
        String accessToken = jwtUtils.generateToken(userDetails);

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken(accessToken);
        jwtResponse.setRefreshToken(refreshToken.getToken());
        logger.info("User authenticated successfully: {}", loginRequest.getUserMail());
        return ResponseEntity.ok(jwtResponse);

    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest, HttpServletResponse response) throws ConflictException {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshTokenRequest.getToken());

        if (optionalRefreshToken.isPresent()) {
            RefreshToken refreshToken = optionalRefreshToken.get();

            if (refreshTokenService.verifyExpiration(refreshToken).isPresent()) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUser().getUserMail());
                String accessToken = jwtUtils.generateToken(userDetails);
                return new JwtResponse(accessToken, refreshTokenRequest.getToken(), null);
            }
        }
        logger.error("Refresh token is not in the database or has expired: {}", refreshTokenRequest.getToken());
        throw new ConflictException("Refresh token is not in the database or has expired!");
    }


    @Override
    public User getUserViaPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user != null) {
            logger.info("User retrieved by phone number: {}", phoneNumber);
        } else {
            logger.warn("User not found by phone number: {}", phoneNumber);
        }
        return user;
    }


    @Override
    public String getEncodedPassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        logger.info("Password encoded for user");
        return encodedPassword;
    }

    @Override
    public User updateUser(User user) {
        User updatedUser = userRepository.save(user);
        logger.info("User updated: {}", user.getUserMail());
        return updatedUser;
    }

    @Override
    public User getUserByMail(String userMail) {
        User user = userRepository.findByUserMail(userMail);
        if (user != null) {
            logger.info("User retrieved by email: {}", userMail);
        } else {
            logger.warn("User not found by email: {}", userMail);
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            logger.info("User retrieved by ID: {}", id);
            return optionalUser.get();
        } else {
            logger.warn("User not found by ID: {}", id);
            throw new NoSuchElementException("User not found for id: " + id);
        }
    }

    @Override
    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        if (isEmailExists(registrationDTO.getUserMail())) {
            logger.error("Email ID already exists: {}", registrationDTO.getUserMail());
            throw new IllegalArgumentException("Email ID already exists");
        }

        User user = new User();
        user.setUserMail(registrationDTO.getUserMail());
        user.setUserPassword(getEncodedPassword(registrationDTO.getPassword()));

        User savedUser = userRepository.save(user);

        String subject = "REGISTRATION PROCESS";
        String message = "You are Successfully Registered and Welcome to the Learn Buddy Platform";
        String to = savedUser.getUserMail(); // Assuming 'getEmail()' returns the user's email address
        emailService.sendEmail(subject, message, to);

        logger.info("User registered successfully: {}", savedUser.getUserMail());
        return savedUser;
    }

    public boolean isEmailExists(String userMail) {
        User existingUser = userRepository.findByUserMail(userMail);
        if (existingUser != null) {
            logger.warn("Email ID already exists: {}", userMail);
        }
        return existingUser != null;
    }


    @Override
    public ResponseEntity<JwtResponse> insertSsoUser(SSOUserDto newSsoUser) {
        User existingUser = findUserByEmail(newSsoUser.getUserMail());
        if (existingUser != null) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(newSsoUser.getUserMail());

            UserDetails userDetails = userDetailsService.loadUserByUsername(existingUser.getUserMail());
            String accessToken = jwtUtils.generateToken(userDetails);
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setAccessToken(accessToken);
            jwtResponse.setRefreshToken(refreshToken.getToken());

            logger.info("User authenticated via SSO: {}", newSsoUser.getUserMail());
            return ResponseEntity.ok(jwtResponse);
        } else {
            User newUser = insertNewUser(newSsoUser);
            if (newUser != null) {
                logger.info("User registered via SSO: {}", newSsoUser.getUserMail());
                JwtResponse jwtResponse = new JwtResponse();
                jwtResponse.setErrorMessage("Registered Successfully");
                return ResponseEntity.ok(jwtResponse);
            } else {
                logger.error("Failed to register user via SSO: {}", newSsoUser.getUserMail());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }


    public User insertNewUser(SSOUserDto newSsoUser) {
        User user = new User();
        user.setUserName(newSsoUser.getUserName());
        user.setUserMail(newSsoUser.getUserMail());

        try {
            // Save the new user to the database
            User savedUser = userRepository.save(user);
            logger.info("User inserted successfully via SSO: {}", newSsoUser.getUserMail());
            return savedUser;
        } catch (Exception e) {
            logger.error("Failed to insert user via SSO: {}", newSsoUser.getUserMail(), e);
            // Handle any exceptions that may occur during user insertion
            return null;
        }
    }

    @Override
    public User findUserByEmail(String userMail) {
        User foundUser = userRepository.findByUserMail(userMail);
        if (foundUser != null) {
            logger.info("User found by email: {}", userMail);
        } else {
            logger.warn("User not found by email: {}", userMail);
        }
        return foundUser;
    }

    public String getMenteeEmailById(Long menteeId) {
        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> {
                    logger.error("Mentee not found by ID: {}", menteeId);
                    throw new NoSuchElementException("Mentee not found by ID: " + menteeId);
                });
        logger.info("Mentee email retrieved by ID: {}", mentee.getUserMail());
        return mentee.getUserMail();
    }

}