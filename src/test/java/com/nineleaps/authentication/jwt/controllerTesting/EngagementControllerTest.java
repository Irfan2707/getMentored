package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.EngagementController;
import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import com.nineleaps.authentication.jwt.exception.DuplicateEngagementException;
import com.nineleaps.authentication.jwt.service.implementation.EngagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EngagementControllerTest {
    @Mock
    private EngagementServiceImpl engagementServiceImpl;

    @Mock
    private Logger logger;

    private EngagementController engagementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        engagementController = new EngagementController(engagementServiceImpl);

    }


    @Test
    void testCreateEngagementSuccess() {
        // Arrange
        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO();
        ResponseEntity<CreateEngagementRequestDTO> mockResponseEntity = ResponseEntity.ok(requestDTO);

        when(engagementServiceImpl.createEngagement(requestDTO)).thenReturn(mockResponseEntity);

        // Act
        ResponseEntity<Object> responseEntity = engagementController.createEngagement(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Engagement created successfully.", responseBody.get("message"));
        assertEquals(requestDTO, responseBody.get("data"));


        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateEngagementDuplicateException() {
        // Arrange
        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO(/* provide necessary data */);

        when(engagementServiceImpl.createEngagement(requestDTO)).thenThrow(DuplicateEngagementException.class);

        // Act
        ResponseEntity<Object> responseEntity = engagementController.createEngagement(requestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Duplicate engagement found.", responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }

    @Test
    void testCreateEngagementGeneralException() {
        // Arrange
        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO();

        when(engagementServiceImpl.createEngagement(requestDTO)).thenThrow(RuntimeException.class);

        // Act
        ResponseEntity<Object> responseEntity = engagementController.createEngagement(requestDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Error while creating engagement.", responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetEngagementDetailsByUserIdNotEmpty() {
        // Arrange
        Long userId = 1L;
        List<Map<String, Object>> engagementDetails = new ArrayList<>();
        engagementDetails.add(createEngagementDetail("Engagement 1"));
        engagementDetails.add(createEngagementDetail("Engagement 2"));

        when(engagementServiceImpl.getEngagementDetailsByUserId(userId)).thenReturn(engagementDetails);

        // Act
        ResponseEntity<Object> responseEntity = engagementController.getEngagementDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Engagement details retrieved successfully.", responseBody.get("message"));
        assertEquals(engagementDetails, responseBody.get("data"));
    }

    private Map<String, Object> createEngagementDetail(String name) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("name", name);
        return detail;
    }


    @Test
    void testGetEngagementDetailsByUserIdNoEngagementsFound() {
        // Arrange
        Long userId = 1L;
        List<Map<String, Object>> emptyEngagementDetails = Collections.emptyList();

        when(engagementServiceImpl.getEngagementDetailsByUserId(userId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = engagementController.getEngagementDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No engagements found for the given user ID.", responseBody.get("error"));


    }
}