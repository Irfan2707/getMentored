package com.nineleaps.authentication.jwt.configurationTesting;

import com.nineleaps.authentication.jwt.configuration.JwtAuthenticationEntryPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jwtAuthenticationEntryPoint).build();
    }

    @Test
    void testCommence() throws IOException, ServletException {
        String errorMessage = "Custom error message";

        // Mock the behavior of HttpServletResponse
        Mockito.doAnswer(invocation -> {
            String message = (String) invocation.getArgument(1);
            HttpServletResponse mockResponse = (HttpServletResponse) invocation.getMock();
            mockResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            assertEquals("Unauthorized", message);
            return null;
        }).when(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);

        // Invoking the commence method
        jwtAuthenticationEntryPoint.commence(request, response, authException);

    }


}
