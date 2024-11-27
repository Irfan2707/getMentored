package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.LoggingAndReportingController;
import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsMentorDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.service.implementation.LoggingAndReportingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoggingAndReportingControllerTest {
    @Mock
    private LoggingAndReportingServiceImpl loggingAndReportingServiceImpl;
    @InjectMocks
    private LoggingAndReportingController loggingAndReportingController;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetConnectionsStatisticsByMentorId_Success() {
        // Mocking the service method to return a specific ConnectionRequestStatisticsMentorDTO
        when(loggingAndReportingServiceImpl.getConnectionsStatisticsByMentorId(anyLong()))
                .thenReturn(new ConnectionRequestStatisticsMentorDTO(10, 5, 3, 2));

        // Calling the actual method in your controller
        ResponseEntity<Object> response = loggingAndReportingController.getConnectionsStatisticsByMentorId(1L);

        // Verify that the service method was called with the correct argument
        verify(loggingAndReportingServiceImpl).getConnectionsStatisticsByMentorId(1L);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        // Assert the response status and message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Connection Request Statistics retrieved successfully.", responseBody.get("message"));
    }

    @Test
    void testGetConnectionsStatisticsByMentorId_Failure() {
        // Mocking the service method to return a ConnectionRequestStatisticsMentorDTO with negative values
        when(loggingAndReportingServiceImpl.getConnectionsStatisticsByMentorId(anyLong()))
                .thenReturn(new ConnectionRequestStatisticsMentorDTO(-1, -1, -1, -1));

        // Calling the actual method in your controller
        ResponseEntity<Object> response = loggingAndReportingController.getConnectionsStatisticsByMentorId(2L);
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        // Verify that the service method was called with the correct argument
        verify(loggingAndReportingServiceImpl).getConnectionsStatisticsByMentorId(2L);

        // Assert the response status and message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

    }


    @Test
    void testGetSlotStatisticsByMentorId_Success() {
        // Mocking the service method to return a specific SlotStatisticsDTO
        when(loggingAndReportingServiceImpl.getSlotStatisticsByMentorId(anyLong()))
                .thenReturn(new SlotStatisticsDTO(20, 10, 5));

        // Calling the actual method in your controller
        ResponseEntity<Object> response = loggingAndReportingController.getSlotStatisticsByMentorId(1L);

        // Verify that the service method was called with the correct argument
        verify(loggingAndReportingServiceImpl).getSlotStatisticsByMentorId(1L);

        // Assert the response status and message
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Assert the content of the response body
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
        assertEquals("Slot Statistics retrieved successfully.", responseBody.get("message"));
    }

    @Test
    void testGetSlotStatisticsByMentorId_Failure() {
        // Mocking the service method to return a SlotStatisticsDTO with negative values
        when(loggingAndReportingServiceImpl.getSlotStatisticsByMentorId(anyLong()))
                .thenReturn(new SlotStatisticsDTO(-1, -1, -1));

        // Calling the actual method in your controller
        ResponseEntity<Object> response = loggingAndReportingController.getSlotStatisticsByMentorId(2L);

        // Verify that the service method was called with the correct argument
        verify(loggingAndReportingServiceImpl).getSlotStatisticsByMentorId(2L);

        // Assert the response status and message
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        // Assert the content of the response body
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);
    }

}