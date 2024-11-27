package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.SlotController;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.service.interfaces.ISlotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotControllerTest {

    @Mock
    private ISlotService ISlotService;
    @InjectMocks
    private SlotController slotController;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetSlotsByMentorId() {
        // Arrange
        Long mentorId = 1L;
        int pageNumber = 0;
        int pageSize = 10;

        // Mock the slotService to return a Page of SlotDTOs
        List<SlotDTO> slots = new ArrayList<>(); // Create an empty list
        Page<SlotDTO> slotsPage = new PageImpl<>(slots);
        when(ISlotService.getSlotsByMentorId(mentorId, pageNumber, pageSize)).thenReturn(slotsPage);

        // Act
        ResponseEntity<List<SlotDTO>> response = slotController.getSlotsByMentorId(mentorId, pageNumber, pageSize);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(slots, response.getBody());
    }


    @Test
    void testCreateSlotSuccess() throws ConflictException {
        // Arrange
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setId(1L);

        when(ISlotService.createSlot(slotDTO)).thenReturn(slotDTO);

        // Act
        ResponseEntity<Object> responseEntity = slotController.createSlot(slotDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Slot created successfully", responseBody.get("message"));
        assertEquals(slotDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateSlotFailure() throws ConflictException {
        // Arrange
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setId(2L);

        when(ISlotService.createSlot(slotDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = slotController.createSlot(slotDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to create the slot.", responseBody.get("error"));


    }


    @Test
    void testUpdateSlotSuccess() throws ResourceNotFoundException {
        // Arrange
        Long slotId = 1L;
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setId(slotId);

        when(ISlotService.updateSlot(slotId, slotDTO)).thenReturn(slotDTO);

        // Act
        ResponseEntity<Object> responseEntity = slotController.updateSlot(slotId, slotDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Slot updated successfully", responseBody.get("message"));
        assertEquals(slotDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testUpdateSlotNotFound() throws ResourceNotFoundException {
        // Arrange
        Long slotId = 2L;
        SlotDTO slotDTO = new SlotDTO();
        slotDTO.setId(slotId);

        when(ISlotService.updateSlot(slotId, slotDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = slotController.updateSlot(slotId, slotDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No slot found with the given ID.", responseBody.get("error"));


    }


    @Test
    void testDeleteSlotSuccess() throws ResourceNotFoundException {
        // Arrange
        Long slotId = 1L;

        // Act
        ResponseEntity<Object> responseEntity = slotController.deleteSlot(slotId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Slot deleted successfully", responseBody.get("message"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testDeleteSlotNotFound() throws ResourceNotFoundException {
        // Arrange
        Long slotId = 2L;

        doThrow(ResourceNotFoundException.class).when(ISlotService).deleteSlot(slotId);

        // Act
        ResponseEntity<Object> responseEntity = slotController.deleteSlot(slotId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No slot found with the given ID.", responseBody.get("error"));

    }


    @Test
    void testDeleteBySlotIdSuccess() {
        // Arrange
        List<Long> slotIds = Arrays.asList(1L, 2L, 3L);

        // Act
        ResponseEntity<Object> responseEntity = slotController.deleteBySlotId(slotIds);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Slots Deleted", responseBody.get("message"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testDeleteBySlotIdFailure() {
        // Arrange
        List<Long> slotIds = Arrays.asList(1L, 2L, 3L);

        doThrow(RuntimeException.class).when(ISlotService).deleteMultipleSlotsById(slotIds);


        // Act
        ResponseEntity<Object> responseEntity = slotController.deleteBySlotId(slotIds);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to delete slots.", responseBody.get("error"));
    }


    @Test
    void testGetSlotCountsByMentorAndDateRangeSuccess() {
        // Arrange
        Long mentorId = 1L;
        String startDateStr = "2023-01-01";
        String endDateStr = "2023-01-31";
        SlotStatisticsDTO slotStatistics = new SlotStatisticsDTO(10, 5, 2);

        when(ISlotService.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr)).thenReturn(slotStatistics);

        // Act
        ResponseEntity<Object> responseEntity = slotController.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Slot counts retrieved successfully", responseBody.get("message"));
        SlotStatisticsDTO response = (SlotStatisticsDTO) responseBody.get("data");
        assertNotNull(response);
        assertEquals(10, response.getTotalSlots());
        assertEquals(5, response.getBookedSlots());
        assertEquals(2, response.getPendingSlots());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetSlotCountsByMentorAndDateRangeFailure() {
        // Arrange
        Long mentorId = 1L;
        String startDateStr = "2023-01-01";
        String endDateStr = "2023-01-31";

        when(ISlotService.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = slotController.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to retrieve slot counts.", responseBody.get("error"));

    }


}