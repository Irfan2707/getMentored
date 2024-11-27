package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.EngagementStatusController;
import com.nineleaps.authentication.jwt.dto.EngagementStatusDTO;
import com.nineleaps.authentication.jwt.enums.EngStatus;
import com.nineleaps.authentication.jwt.service.implementation.EngagementStatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EngagementStatusControllerTest {

    @Mock
    private EngagementStatusServiceImpl engagementStatusServiceImpl;

    @Mock
    private Logger logger;

    private EngagementStatusController engagementStatusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        engagementStatusController = new EngagementStatusController(engagementStatusServiceImpl);
    }

    @Test
    void testCreateOrUpdateEngagementStatusSuccess() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
        engagementStatusDTO.setMentorEngStatus(EngStatus.DONE);
        engagementStatusDTO.setMenteeEngStatus(EngStatus.DONE);

        when(engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO)).thenReturn(ResponseEntity.ok(engagementStatusDTO));

        // Act
        ResponseEntity<Object> responseEntity = engagementStatusController.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Engagement status created or updated successfully.", responseBody.get("message"));
        assertEquals(engagementStatusDTO, responseBody.get("data"));


    }

    @Test
    void testCreateOrUpdateEngagementStatusFailure() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
        engagementStatusDTO.setEngagementId(engagementId);

        ResponseEntity<EngagementStatusDTO> failureResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new EngagementStatusDTO());

        when(engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO)).thenReturn(failureResponse);

        // Act
        ResponseEntity<Object> responseEntity = engagementStatusController.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to create or update engagement status.", responseBody.get("error"));
    }


    @Test
    void testGetEngagementStatusByEngagementIdSuccess() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
        engagementStatusDTO.setMentorEngStatus(EngStatus.DONE);
        engagementStatusDTO.setMenteeEngStatus(EngStatus.DONE);


        when(engagementStatusServiceImpl.getEngagementStatusByEngagementId(engagementId)).thenReturn(ResponseEntity.ok(engagementStatusDTO));

        // Act
        ResponseEntity<Object> responseEntity = engagementStatusController.getEngagementStatusByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Engagement status retrieved successfully.", responseBody.get("message"));
        assertEquals(engagementStatusDTO, responseBody.get("data"));


    }

    @Test
    void testGetEngagementStatusByEngagementIdNotFound() {
        // Arrange
        Long engagementId = 1L;

        when(engagementStatusServiceImpl.getEngagementStatusByEngagementId(engagementId))
                .thenReturn(ResponseEntity.notFound().build());

        // Act
        ResponseEntity<Object> responseEntity = engagementStatusController.getEngagementStatusByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Engagement status not found.", responseBody.get("error"));
    }

}
