package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.JwtResponse;
import com.nineleaps.authentication.jwt.dto.RefreshTokenRequest;
import com.nineleaps.authentication.jwt.dto.SSOUserDto;
import com.nineleaps.authentication.jwt.dto.UserRegistrationDTO;
import com.nineleaps.authentication.jwt.entity.AuthenticationRequest;
import com.nineleaps.authentication.jwt.entity.RefreshToken;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.RefreshTokenService;
import com.nineleaps.authentication.jwt.service.implementation.UserServiceImpl;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailServiceImpl emailServiceImpl;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private JwtUtils jwtUtils;


    @Mock
    private RefreshToken refreshToken;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testAuthenticate_ValidCredentials_ReturnsJwtResponse() {
        // Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest("user@example.com", "password");
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock User object
        User mockUser = new User();
        mockUser.setUserMail("user@example.com");

        when(userRepository.findByUserMail("user@example.com")).thenReturn(mockUser);

        // Mock the behavior of authenticationManager
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // Mock UserDetailsService to return a mock UserDetails
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(mock(UserDetails.class));

        // Mock JWT token generation to return a fake access token
        when(jwtUtils.generateToken(any())).thenReturn("fakeAccessToken");

        RefreshToken mockRefreshToken = new RefreshToken();
        when(refreshTokenService.createRefreshToken(loginRequest.getUserMail())).thenReturn(mockRefreshToken);
        // Mock RefreshTokenService to return an empty Optional when verifyExpiration is called
        when(refreshTokenService.verifyExpiration(any())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> result = userServiceImpl.authenticate(loginRequest, response);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) result.getBody();
        assertNotNull(jwtResponse.getAccessToken());
        assertNull(jwtResponse.getRefreshToken());

    }



    @Test
    void testAuthenticate_InvalidCredentials_ReturnsUnauthorized() {
        // Arrange
        AuthenticationRequest loginRequest = new AuthenticationRequest("user@example.com", "invalidPassword");
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        // Act
        ResponseEntity<?> result = userServiceImpl.authenticate(loginRequest, response);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid email or password", result.getBody());
    }
     @Test
     void testAuthenticate_UserNotFound_ReturnsNotFound() {
         // Arrange
         AuthenticationRequest loginRequest = new AuthenticationRequest("nonexistent@example.com", "password");
         HttpServletResponse response = mock(HttpServletResponse.class);

         when(userRepository.findByUserMail("nonexistent@example.com")).thenReturn(null);

         // Act
         ResponseEntity<?> result = userServiceImpl.authenticate(loginRequest, response);

         // Assert
         assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
         assertNull(result.getBody());
     }



     @Test
void testRefreshToken_ValidToken_ReturnsJwtResponse() throws ConflictException {
    // Arrange
    RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("fakeRefreshToken");

    // Mock the RefreshTokenService to return a mock RefreshToken
    RefreshToken mockRefreshToken = new RefreshToken();
    mockRefreshToken.setToken("fakeRefreshToken");

    // Mock the associated User object
    User mockUser = new User();
    mockUser.setUserMail("user@example.com");
    mockRefreshToken.setUser(mockUser);

    when(refreshTokenService.findByToken("fakeRefreshToken")).thenReturn(Optional.of(mockRefreshToken));

    // Mock the verifyExpiration method to return a non-empty Optional (indicating not expired)
    when(refreshTokenService.verifyExpiration(mockRefreshToken)).thenReturn(Optional.of(mockRefreshToken));

    // Mock the UserDetails and JWT token generation
    UserDetails mockUserDetails = mock(UserDetails.class);
    when(userDetailsService.loadUserByUsername(mockUser.getUserMail())).thenReturn(mockUserDetails);
    when(jwtUtils.generateToken(mockUserDetails)).thenReturn("fakeAccessToken");

    // Act
    JwtResponse result = userServiceImpl.refreshToken(refreshTokenRequest, mock(HttpServletResponse.class));

    // Assert
    assertNotNull(result);
    assertNotNull(result.getAccessToken());
    assertNull(result.getErrorMessage());
}





    @Test
    void testRefreshToken_InvalidToken_ThrowsConflictException() {
        // Arrange
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest("fakeRefreshToken");
        when(refreshTokenService.findByToken("fakeRefreshToken")).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ConflictException.class, () -> userServiceImpl.refreshToken(refreshTokenRequest, mock(HttpServletResponse.class)));
    }

    @Test
    void testGetUserViaPhoneNumber_UserExists_ReturnsUser() {
        // Arrange
        String phoneNumber = "1234567890";
        User mockUser = new User();
        mockUser.setPhoneNumber(phoneNumber);

        // Mock UserRepository to return the mockUser when findByPhoneNumber is called
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(mockUser);

        // Act
        User result = userServiceImpl.getUserViaPhoneNumber(phoneNumber);

        // Assert
        assertNotNull(result);
        assertEquals(phoneNumber, result.getPhoneNumber());
        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber); // Verify that the method was called
    }

    @Test
    void testGetUserViaPhoneNumber_UserDoesNotExist_ReturnsNull() {
        // Arrange
        String phoneNumber = "1234567890";

        // Mock UserRepository to return null when findByPhoneNumber is called
        when(userRepository.findByPhoneNumber(phoneNumber)).thenReturn(null);

        // Act
        User result = userServiceImpl.getUserViaPhoneNumber(phoneNumber);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByPhoneNumber(phoneNumber); // Verify that the method was called
    }

//    @Test
//    void testDeleteUserById_UserExists_DeletesUser() {
//        // Arrange
//        Long userId = 1L;
//
//        // Mock UserRepository to perform deletion successfully
//        doNothing().when(userRepository).deleteById(userId);
//
//        // Act
//        userServiceImpl.deleteUserById(userId);
//
//        // Assert
//        verify(userRepository, times(1)).deleteById(userId); // Verify that the deleteById method was called
//    }
//
//     @Test
//     void testDeleteUserById_UserDoesNotExist_DoesNotDelete() {
//         // Arrange
//         Long userId = 1L;
//
//         // Mock UserRepository to throw the appropriate exception (e.g., EmptyResultDataAccessException) when deleting
//         doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(userId);
//
//         // Act and Assert
//         assertThrows(EmptyResultDataAccessException.class, () -> userServiceImpl.deleteUserById(userId));
//         verify(userRepository, times(1)).deleteById(userId); // Verify that the deleteById method was called
//     }


    @Test
    void testGetEncodedPassword() {
        // Arrange
        String password = "password";
        String encodedPassword = "encodedPassword";

        // Mock PasswordEncoder to return encodedPassword
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        // Act
        String result = userServiceImpl.getEncodedPassword(password);

        // Assert
        assertNotNull(result);
        assertEquals(encodedPassword, result);
        verify(passwordEncoder, times(1)).encode(password); // Verify that the encode method was called
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User user = new User();
        user.setUserMail("user@example.com");

        // Mock UserRepository to return the updated user
        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userServiceImpl.updateUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getUserMail(), result.getUserMail());
        verify(userRepository, times(1)).save(user); // Verify that the save method was called
    }

    @Test
    void testGetUserByMail_UserExists() {
        // Arrange
        String userMail = "user@example.com";
        User user = new User();
        user.setUserMail(userMail);

        // Mock UserRepository to return the user
        when(userRepository.findByUserMail(userMail)).thenReturn(user);

        // Act
        User result = userServiceImpl.getUserByMail(userMail);

        // Assert
        assertNotNull(result);
        assertEquals(userMail, result.getUserMail());
        verify(userRepository, times(1)).findByUserMail(userMail); // Verify that the findByUserMail method was called
    }

    @Test
    void testGetUserByMail_UserDoesNotExist() {
        // Arrange
        String userMail = "nonexistent@example.com";

        // Mock UserRepository to return null (user not found)
        when(userRepository.findByUserMail(userMail)).thenReturn(null);

        // Act
        User result = userServiceImpl.getUserByMail(userMail);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).findByUserMail(userMail); // Verify that the findByUserMail method was called
    }


    @Test
    void testGetUserById_UserExists() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        // Mock UserRepository to return an optional containing the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userServiceImpl.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        verify(userRepository, times(1)).findById(userId); // Verify that the findById method was called
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        // Arrange
        Long userId = 1L;

        // Mock UserRepository to return an empty optional (user not found)
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> userServiceImpl.getUserById(userId));
        verify(userRepository, times(1)).findById(userId); // Verify that the findById method was called
    }

    @Test
    void testRegisterNewUser_SuccessfulRegistration() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUserMail("newuser@example.com");
        registrationDTO.setPassword("password");
        registrationDTO.setPhoneNumber("1234567890"); // Set a phone number if needed

        // Mock UserRepository to return null (user does not exist)
        when(userRepository.findByUserMail(registrationDTO.getUserMail())).thenReturn(null);

        User savedUser = new User();
        savedUser.setUserMail(registrationDTO.getUserMail());

        // Mock UserRepository to return the saved user
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Mock password encoding
        when(passwordEncoder.encode(registrationDTO.getPassword())).thenReturn("encodedPassword");

        // Act
        User result = userServiceImpl.registerNewUser(registrationDTO);

        // Assert
        assertNotNull(result);
        assertEquals(registrationDTO.getUserMail(), result.getUserMail());

        verify(userRepository, times(1)).findByUserMail(registrationDTO.getUserMail());

        verify(emailServiceImpl, times(1)).sendEmail("REGISTRATION PROCESS", "You are Successfully Registered and Welcome to the Learn Buddy Platform", registrationDTO.getUserMail());
    }

    @Test
    void testRegisterNewUser_EmailAlreadyExists() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUserMail("existing@example.com");
        registrationDTO.setPassword("password");

        // Mock UserRepository to return an existing user
        when(userRepository.findByUserMail(registrationDTO.getUserMail())).thenReturn(new User());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userServiceImpl.registerNewUser(registrationDTO);
        });

        // Verify that findByUserMail method was called once
        verify(userRepository, times(1)).findByUserMail(registrationDTO.getUserMail());

        // Verify that the save method was not called
        verify(userRepository, never()).save(any(User.class));

        // Verify that the emailService.sendEmail method was not called
        verify(emailServiceImpl, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testRegisterNewUser_EmailExists() {
        // Arrange
        UserRegistrationDTO registrationDTO = new UserRegistrationDTO();
        registrationDTO.setUserMail("existing@example.com");
        registrationDTO.setPassword("password");

        // Mock UserRepository to return an existing user
        User existingUser = new User();
        when(userRepository.findByUserMail(registrationDTO.getUserMail())).thenReturn(existingUser);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userServiceImpl.registerNewUser(registrationDTO));
        verify(userRepository, times(1)).findByUserMail(registrationDTO.getUserMail()); // Verify that the findByUserMail method was called
        verify(userRepository, never()).save((SSOUserDto) any()); // Ensure that the save method was not called
        verify(emailServiceImpl, never()).sendEmail(any(), any(), any()); // Ensure that sendEmail method was not called
    }

    @Test
    void testIsEmailExists_EmailExists() {
        // Arrange
        String existingEmail = "existing@example.com";

        // Mock UserRepository to return an existing user
        when(userRepository.findByUserMail(existingEmail)).thenReturn(new User());

        // Act
        boolean result = userServiceImpl.isEmailExists(existingEmail);

        // Assert
        assertTrue(result);

        // Verify that findByUserMail method was called once
        verify(userRepository, times(1)).findByUserMail(existingEmail);
    }

    @Test
    void testIsEmailExists_EmailDoesNotExist() {
        // Arrange
        String nonExistingEmail = "newuser@example.com";

        // Mock UserRepository to return null (user does not exist)
        when(userRepository.findByUserMail(nonExistingEmail)).thenReturn(null);

        // Act
        boolean result = userServiceImpl.isEmailExists(nonExistingEmail);

        // Assert
        assertFalse(result);

        // Verify that findByUserMail method was called once
        verify(userRepository, times(1)).findByUserMail(nonExistingEmail);
    }
//Inserting SSO User

     @Test
     void testInsertSsoUser_SuccessfulAuthentication() {
         // Arrange
         SSOUserDto newSsoUser = new SSOUserDto();
         newSsoUser.setUserMail("existinguser@example.com");

         // Mock UserRepository to return an existing user when findByUserMail is called
         User existingUser = new User();
         existingUser.setUserMail(newSsoUser.getUserMail());
         when(userRepository.findByUserMail(newSsoUser.getUserMail())).thenReturn(existingUser);

         // Mock RefreshTokenService to return a mock refresh token
         RefreshToken mockRefreshToken = new RefreshToken();
         when(refreshTokenService.createRefreshToken(newSsoUser.getUserMail())).thenReturn(mockRefreshToken);

         // Mock UserDetails from UserDetailsService to return a mock UserDetails
         UserDetails mockUserDetails = mock(UserDetails.class);
         when(userDetailsService.loadUserByUsername(newSsoUser.getUserMail())).thenReturn(mockUserDetails);

         // Mock JWT token generation to return a fake access token
         when(jwtUtils.generateToken(mockUserDetails)).thenReturn("fakeAccessToken");

         // Act
         ResponseEntity<JwtResponse> result = userServiceImpl.insertSsoUser(newSsoUser);

         // Assert
         assertEquals(HttpStatus.OK, result.getStatusCode());
         assertNotNull(result.getBody());

     }

     @Test
     void testInsertSsoUser_SuccessfulRegistration() {
         // Arrange
         SSOUserDto newSsoUser = new SSOUserDto();
         newSsoUser.setUserMail("newuser@example.com");

         // Mock UserRepository to return null, indicating that the user does not exist
         when(userRepository.findByUserMail(newSsoUser.getUserMail())).thenReturn(null);

         // Mock the insertNewUser method to return a new user
         User newUser = new User();
         newUser.setUserMail(newSsoUser.getUserMail());
         when(userRepository.save(any(User.class))).thenReturn(newUser);

         // Act
         ResponseEntity<JwtResponse> result = userServiceImpl.insertSsoUser(newSsoUser);

         // Assert
         assertEquals(HttpStatus.OK, result.getStatusCode());
         assertNotNull(result.getBody());
         assertEquals("Registered Successfully", result.getBody().getErrorMessage());
     }

     @Test
     void testInsertSsoUser_FailedRegistration() {
         // Arrange
         SSOUserDto newSsoUser = new SSOUserDto();
         newSsoUser.setUserMail("newuser@example.com");

         // Mock UserRepository to return null, indicating that the user does not exist
         when(userRepository.findByUserMail(newSsoUser.getUserMail())).thenReturn(null);

         // Mock the insertNewUser method to return null, indicating a failed registration
         when(userServiceImpl.insertNewUser(newSsoUser)).thenReturn(null);

         // Act
         ResponseEntity<JwtResponse> result = userServiceImpl.insertSsoUser(newSsoUser);

         // Assert
         assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
         assertNull(result.getBody());
     }







    @Test
    void testInsertNewUser_Success() {
        // Arrange
        SSOUserDto newSsoUser = new SSOUserDto();
        newSsoUser.setUserName("New User");
        newSsoUser.setUserMail("newuser@example.com");

        // Mock UserRepository to return the saved user
        User savedUser = new User();
        savedUser.setUserName(newSsoUser.getUserName());
        savedUser.setUserMail(newSsoUser.getUserMail());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userServiceImpl.insertNewUser(newSsoUser);

        // Assert
        assertNotNull(result);
        assertEquals(newSsoUser.getUserName(), result.getUserName());
        assertEquals(newSsoUser.getUserMail(), result.getUserMail());
        verify(userRepository, times(1)).save(any(User.class)); }

    @Test
    void testInsertNewUser_Failure() {
        // Arrange
        SSOUserDto newSsoUser = new SSOUserDto();
        newSsoUser.setUserName("New User");
        newSsoUser.setUserMail("newuser@example.com");

        // Mock UserRepository to throw an exception when saving
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Failed to save user"));

        // Act
        User result = userServiceImpl.insertNewUser(newSsoUser);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void testFindUserByEmail_UserExists() {
        // Arrange
        String userEmail = "user@example.com";
        User mockUser = new User();
        mockUser.setUserMail(userEmail);

        // Mock UserRepository to return the user when findByUserMail is called
        when(userRepository.findByUserMail(userEmail)).thenReturn(mockUser);

        // Act
        User result = userServiceImpl.findUserByEmail(userEmail);

        // Assert
        assertNotNull(result);
        assertEquals(userEmail, result.getUserMail());
    }

    @Test
    void testFindUserByEmail_UserDoesNotExist() {
        // Arrange
        String userEmail = "nonexistent@example.com";

        // Mock UserRepository to return null when findByUserMail is called
        when(userRepository.findByUserMail(userEmail)).thenReturn(null);

        // Act
        User result = userServiceImpl.findUserByEmail(userEmail);

        // Assert
        assertNull(result);
    }

    @Test
    void testGetMenteeEmailById_MenteeExists() {
        // Arrange
        Long menteeId = 123L;
        String menteeEmail = "mentee@example.com";
        User mockMentee = new User();
        mockMentee.setUserMail(menteeEmail);

        // Mock UserRepository to return an Optional containing the mentee when findById is called
        when(userRepository.findById(menteeId)).thenReturn(Optional.of(mockMentee));

        // Act
        String result = userServiceImpl.getMenteeEmailById(menteeId);

        // Assert
        assertEquals(menteeEmail, result);
    }

    @Test
    void testGetMenteeEmailById_MenteeDoesNotExist() {
        // Arrange
        Long menteeId = 456L;

        // Mock UserRepository to return an empty Optional when findById is called
        when(userRepository.findById(menteeId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> userServiceImpl.getMenteeEmailById(menteeId));

    }
}



