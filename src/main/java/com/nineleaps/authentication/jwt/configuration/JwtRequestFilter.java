package com.nineleaps.authentication.jwt.configuration;

import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.service.implementation.JwtUserDetailsServiceImpl;
import com.nineleaps.authentication.jwt.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;
    private final Logger loggers = LoggerFactory.getLogger(JwtRequestFilter.class);


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            try {

                if (Boolean.TRUE.equals(jwtUtils.validateToken(jwt))) {

                    loggers.info("jwt token: {}", jwt);
                    String userMail = jwtUtils.extractEmail(jwt);
                    loggers.info("User mail : {}", jwt);
                    String phoneNumber = jwtUtils.extractPhoneNumber(jwt);
                    loggers.info("Users phone number: {}", phoneNumber);

                    // Check if userMail and phoneNumber are not null or empty

                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(userMail);
                    loggers.info("Retrieving user details:");

                    // Create a new instance of User and set the phone number
                    User customUserDetails = new User(userMail, phoneNumber, null);
                    customUserDetails.getId();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                } else {
                    // Token validation failed, handle the error
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                    return;

                }
            } catch (Exception e) {
                // Log the error message using your preferred logging framework
                loggers.error("Exception occurred during token validation: {} ", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token validation error");
                return;

            }
        }
        chain.doFilter(request, response);
    }
}



