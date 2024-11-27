package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.GoalTrackerController;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import com.nineleaps.authentication.jwt.service.interfaces.IGoalTrackerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoalTrackerControllerTest {

    @InjectMocks
    private GoalTrackerController goalTrackerController;

    @Mock
    private IGoalTrackerService IGoalTrackerService;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGoalTrackerSuccess() {
        // Arrange
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO();
        when(IGoalTrackerService.createGoalTracker(goalTrackerDTO)).thenReturn(goalTrackerDTO);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.createGoalTracker(goalTrackerDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Goal Tracker created successfully.", responseBody.get("message"));
        assertEquals(goalTrackerDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateGoalTrackerFailure() {
        // Arrange
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO(); // Initialize with necessary values
        when(IGoalTrackerService.createGoalTracker(goalTrackerDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.createGoalTracker(goalTrackerDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to create Goal Tracker.", responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }

    @Test
    void testUpdateGoalTrackerSuccess() throws ResourceNotFoundException {
        // Arrange
        Long id = 1L;
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO(); // Initialize with necessary values
        when(IGoalTrackerService.updateGoalTracker(id, goalTrackerDTO)).thenReturn(goalTrackerDTO);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.updateGoalTracker(id, goalTrackerDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Check the response body structure
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the message and data
        assertEquals("Goal Tracker updated successfully.", responseBody.get("message"));
        assertEquals(goalTrackerDTO, responseBody.get("data"));

        // Ensure that the logger.error was never called
        verify(logger, never()).error(anyString());
    }

    @Test
    void testUpdateGoalTrackerFailure() throws ResourceNotFoundException {
        // Arrange
        Long id = 1L; // Replace with a valid goal tracker ID
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO(); // Initialize with necessary values
        when(IGoalTrackerService.updateGoalTracker(id, goalTrackerDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.updateGoalTracker(id, goalTrackerDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        // Check the response body structure
        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the error message
        assertEquals("Failed to update Goal Tracker.", responseBody.get("error"));

        // Ensure that the logger.info was never called
        verify(logger, never()).info(anyString());
    }

    @Test
    void testDeleteGoalTrackerSuccess() throws ResourceNotFoundException {
        // Arrange
        Long id = 1L;
        doNothing().when(IGoalTrackerService).deleteGoalTracker(id);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.deleteGoalTracker(id);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Goal Tracker deleted successfully.", responseBody.get("message"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testDeleteGoalTrackerFailure() throws ResourceNotFoundException {
        // Arrange
        Long id = 1L;
        doThrow(ResourceNotFoundException.class).when(IGoalTrackerService).deleteGoalTracker(id);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.deleteGoalTracker(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to delete Goal Tracker.", responseBody.get("error"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testGetAllGoalTrackersByEngagementIdSuccess() {
        // Arrange
        Long engagementId = 1L;
        List<GoalTrackerDTO> goalTrackers = new ArrayList<>();
        goalTrackers.add(new GoalTrackerDTO());

        when(IGoalTrackerService.getAllGoalTrackersByEngagementId(engagementId)).thenReturn(goalTrackers);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.getAllGoalTrackersByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Goal Trackers retrieved successfully.", responseBody.get("message"));
        assertEquals(goalTrackers, responseBody.get("data"));

        verify(logger, never()).error(anyString());
        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAllGoalTrackersByEngagementIdEmptyList() {
        // Arrange
        Long engagementId = 2L;
        List<GoalTrackerDTO> goalTrackers = new ArrayList<>();

        when(IGoalTrackerService.getAllGoalTrackersByEngagementId(engagementId)).thenReturn(goalTrackers);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.getAllGoalTrackersByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No Goal Trackers found for the given Engagement ID.", responseBody.get("error"));

        verify(logger, never()).error(anyString());

    }

    @Test
    void testGetAllGoalTrackersByEngagementIdFailure() {
        // Arrange
        Long engagementId = 3L;

        when(IGoalTrackerService.getAllGoalTrackersByEngagementId(engagementId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = goalTrackerController.getAllGoalTrackersByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to retrieve Goal Trackers.", responseBody.get("error"));


        verify(logger, never()).warn(anyString());
    }

}
