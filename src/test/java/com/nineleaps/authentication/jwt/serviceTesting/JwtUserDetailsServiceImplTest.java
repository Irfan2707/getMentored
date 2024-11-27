package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class JwtUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtUserDetailsServiceImpl = new JwtUserDetailsServiceImpl(userRepository);
    }

    @Test
    void testLoadUserByUsername_UserFoundWithPassword() {
        // Arrange
        String userMail = "test@example.com";
        User user = new User();
        user.setUserMail(userMail);
        user.setUserPassword("password");

        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);

        user.setRoles(roles);

        when(userRepository.findByUserMail(userMail)).thenReturn(user);

        // Act
        UserDetails userDetails = jwtUserDetailsServiceImpl.loadUserByUsername(userMail);

        // Assert
        assertNotNull(userDetails);
        assertEquals(userMail, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(Collections.singleton(new SimpleGrantedAuthority(UserRole.MENTEE.name()))));
    }


    @Test
    void testLoadUserByUsername_UserFoundWithoutPassword() {
        // Arrange
        String userMail = "test@example.com";
        User user = new User();
        user.setUserMail(userMail);


        Set<UserRole> roles = new HashSet<>();
        roles.add(UserRole.MENTEE);

        user.setRoles(roles);

        when(userRepository.findByUserMail(userMail)).thenReturn(user);

        // Act
        UserDetails userDetails = jwtUserDetailsServiceImpl.loadUserByUsername(userMail);

        // Assert
        assertNotNull(userDetails);
        assertEquals(userMail, userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().containsAll(Collections.singleton(new SimpleGrantedAuthority(UserRole.MENTEE.name()))));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String userMail = "nonexistent@example.com";
        when(userRepository.findByUserMail(userMail)).thenReturn(null);

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> jwtUserDetailsServiceImpl.loadUserByUsername(userMail));
    }
}
