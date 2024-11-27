package com.nineleaps.authentication.jwt.configurationTesting;

import com.nineleaps.authentication.jwt.configuration.JwtRequestFilter;
import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtRequestFilterTest {

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private JwtUserDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;
    @Mock
    private Logger logger;

    private final String validJwt = "valid_jwt_token";
    private final String userMail = "test@example.com";
    private final String phoneNumber = "+1234567890";

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void shouldHandleInvalidJwtToken() throws Exception {
        // Create a mock HttpServletRequest, HttpServletResponse, and FilterChain
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);
//      Act
        String invalidJwt = "invalid_token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + invalidJwt);
        when(jwtUtils.validateToken(invalidJwt)).thenReturn(false);

        // Execute the filter's doFilterInternal method
        jwtRequestFilter.doFilterInternal(request, response, chain);

        // Verify that the response is set to SC_UNAUTHORIZED with the specified error message
        Mockito.verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        Mockito.verify(chain, Mockito.never()).doFilter(request, response);
    }


    @Test
    void shouldHandleExceptionDuringTokenValidation() throws Exception {
        // Create a mock HttpServletRequest, HttpServletResponse, and FilterChain
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        String validJwt = "valid_token";
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);
        Mockito.when(jwtUtils.validateToken(validJwt)).thenThrow(new RuntimeException("Token validation error"));

        // Execute the filter's doFilterInternal method
        jwtRequestFilter.doFilterInternal(request, response, chain);

        // Verify that the response is set to SC_UNAUTHORIZED with the specified error message
        Mockito.verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation error");

        Mockito.verify(chain, Mockito.never()).doFilter(request, response);
    }

    @Test
    void shouldProcessValidJwtToken() throws Exception {
        // Mock HttpServletRequest, HttpServletResponse, and FilterChain
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        FilterChain chain = Mockito.mock(FilterChain.class);

        String validJwt = "valid_token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);

        // Mock behavior of JwtUtils
        Mockito.when(jwtUtils.validateToken(validJwt)).thenReturn(true);
        Mockito.when(jwtUtils.extractEmail(validJwt)).thenReturn("user@example.com");
        Mockito.when(jwtUtils.extractPhoneNumber(validJwt)).thenReturn("1234567890");

        // Mock UserDetails and userDetailsService behavior
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);
        Mockito.when(userDetails.getAuthorities()).thenReturn(Arrays.asList());

        // Execute the filter's doFilterInternal method
        jwtRequestFilter.doFilterInternal(request, response, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

    }


}
