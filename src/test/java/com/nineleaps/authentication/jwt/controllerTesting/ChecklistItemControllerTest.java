package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.ChecklistItemController;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import com.nineleaps.authentication.jwt.service.implementation.ChecklistItemServiceImpl;
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

class ChecklistItemControllerTest {

    @InjectMocks
    private ChecklistItemController checklistItemController;

    @Mock
    private ChecklistItemServiceImpl checklistItemServiceImpl;
    @Mock
    private Logger logger;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testGetPaginatedChecklistItemsByGoalTrackerId() {
        // Arrange
        Long goalTrackerId = 1L;
        int pageNumber = 0; // Page number
        int pageSize = 10; // Page size
        List<ChecklistItemDTO> checklistItems = new ArrayList<>();

        // Mock the service method with pagination
        when(checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(pageNumber, pageSize, goalTrackerId))
                .thenReturn(checklistItems);

        // Act
        List<ChecklistItemDTO> response = checklistItemController.getAllChecklistItemsByGoalTrackerId(
                goalTrackerId, pageNumber, pageSize);

        // Assert
        assertNotNull(response);
        assertEquals(checklistItems, response);
    }

    @Test
    void testCreateChecklistItemSuccess() {
        // Arrange
        Long userId = 1L;
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();
        checklistItemDTO.setItemDescription("Sample Checklist Item");

        when(checklistItemServiceImpl.createChecklistItem(userId, checklistItemDTO)).thenReturn(checklistItemDTO);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.createChecklistItem(userId, checklistItemDTO);


        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        // Assert
        assertNotNull(responseBody);
        assertEquals("Checklist item created successfully.", responseBody.get("message"));
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(checklistItemDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateChecklistItemFailure() {
        // Arrange
        Long userId = 1L;
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();

        when(checklistItemServiceImpl.createChecklistItem(userId, checklistItemDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.createChecklistItem(userId, checklistItemDTO);


        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Failed to create checklist item.", responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }


    @Test
    void testUpdateChecklistItemStatusSuccess() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long checklistItemId = 12L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;
        ChecklistItemDTO updatedChecklistItem = new ChecklistItemDTO();
        updatedChecklistItem.setId(checklistItemId);
        updatedChecklistItem.setStatus(newStatus);

        when(checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus)).thenReturn(updatedChecklistItem);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.updateChecklistItemStatus(userId, checklistItemId, newStatus);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Checklist item status updated successfully.", responseBody.get("message"));
        assertEquals(checklistItemId, ((ChecklistItemDTO) responseBody.get("data")).getId());
        assertEquals(newStatus, ((ChecklistItemDTO) responseBody.get("data")).getStatus());

    }

    @Test
    void testUpdateChecklistItemStatusFailure() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long checklistItemId = 123L;
        ChecklistitemStatus newStatus = ChecklistitemStatus.DONE;

        when(checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.updateChecklistItemStatus(userId, checklistItemId, newStatus);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Failed to update checklist item status.", responseBody.get("error"));

    }


    @Test
    void testUpdateChecklistItemSuccess() throws ResourceNotFoundException {
        // Arrange
        Long id = 123L;
        Long userId = 1L;
        ChecklistItemDTO checklistItemDto = new ChecklistItemDTO();
        checklistItemDto.setItemDescription("Updated Checklist Item");
        ChecklistItemDTO updatedChecklistItem = new ChecklistItemDTO();
        updatedChecklistItem.setId(id);
        updatedChecklistItem.setItemDescription("Updated Checklist Item");

        when(checklistItemServiceImpl.updateChecklistItem(id, checklistItemDto, userId)).thenReturn(updatedChecklistItem);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.updateChecklistItem(id, checklistItemDto, userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Checklist item updated successfully.", responseBody.get("message"));
        assertEquals(id, ((ChecklistItemDTO) responseBody.get("data")).getId());
        assertEquals("Updated Checklist Item", ((ChecklistItemDTO) responseBody.get("data")).getItemDescription());


    }

    @Test
    void testUpdateChecklistItemFailure() throws ResourceNotFoundException {
        // Arrange
        Long id = 123L;
        Long userId = 1L;
        ChecklistItemDTO checklistItemDto = new ChecklistItemDTO();

        when(checklistItemServiceImpl.updateChecklistItem(id, checklistItemDto, userId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.updateChecklistItem(id, checklistItemDto, userId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("Failed to update checklist item.", responseBody.get("error"));


    }

    @Test
    void testGetActivityDetailsByGoalSuccess() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long goalId = 123L;
        ActivityLogDTO activityLogDTO = new ActivityLogDTO();
        activityLogDTO.setId(1L);
        activityLogDTO.setChecklistId(1L);
        activityLogDTO.setItemDescription("Checlist item description!!");
        List<ActivityLogDTO> activityLogs = new ArrayList<>();
        activityLogs.add(activityLogDTO);
        // Populate activityLogs as needed...

        when(checklistItemServiceImpl.getActivityDetailsByGoal(userId, goalId)).thenReturn(activityLogs);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.getActivityDetailsByGoal(userId, goalId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Activity history retrieved successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data")); // Data should not be null in this case
    }


    @Test
    void testGetActivityDetailsByGoalNoHistoryFound() throws ResourceNotFoundException {
        // Arrange
        Long userId = 1L;
        Long goalId = 123L;
        List<ActivityLogDTO> emptyActivityLogs = new ArrayList<>();

        when(checklistItemServiceImpl.getActivityDetailsByGoal(userId, goalId)).thenReturn(emptyActivityLogs);

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.getActivityDetailsByGoal(userId, goalId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals("No activity history found for the given user and goal.", responseBody.get("error"));


    }

    @Test
    void testDeleteChecklistItemSuccess() {
        // Arrange
        Long checklistItemId = 1L;

        // Act
        ResponseEntity<Object> responseEntity = checklistItemController.deleteChecklistItem(checklistItemId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Checklist item deleted successfully.", responseBody.get("message"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }


}
