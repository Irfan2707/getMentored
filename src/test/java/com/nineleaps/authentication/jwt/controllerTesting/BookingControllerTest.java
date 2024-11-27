package com.nineleaps.authentication.jwt.controllerTesting;


import com.nineleaps.authentication.jwt.controller.BookingController;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.BookingDTO;
import com.nineleaps.authentication.jwt.service.interfaces.IBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private IBookingService IBookingService;

    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookingSuccess() {
        // Arrange
        BookingDTO requestDTO = new BookingDTO();
        BookingDTO expectedResponseDTO = new BookingDTO();
        expectedResponseDTO.setId(1L);

        when(IBookingService.createBooking(requestDTO)).thenReturn(expectedResponseDTO);

        // Act
        ResponseEntity<Object> responseEntity = bookingController.createBooking(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Booking created successfully", responseBody.get("message"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateBookingFailure() {
        // Arrange
        BookingDTO requestDTO = new BookingDTO();
        when(IBookingService.createBooking(requestDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = bookingController.createBooking(requestDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Failed to create booking.", responseBody.get("error"));
    }


    @Test
    void testDeleteMultipleBookings() throws ResourceNotFoundException {
        // Arrange
        List<Long> bookingIds = new ArrayList<>();
        bookingIds.add(1L);
        bookingIds.add(2L);

        Mockito.lenient().doNothing().when(IBookingService).deleteMultipleBookingsById(bookingIds);

        // Act
        ResponseEntity<Object> response = bookingController.deleteMultipleBookings(bookingIds);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        assertNotNull(responseBody);


        assertNull(responseBody.get("data"));
        assertEquals("Bookings deleted successfully", responseBody.get("message"));
    }


    @Test
    void testDeleteBooking() throws ResourceNotFoundException {
        // Arrange
        Long id = 123L;

        Mockito.lenient().doNothing().when(IBookingService).deleteBooking(id);

        // Act
        ResponseEntity<Object> response = bookingController.deleteBooking(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertNotNull(responseBody);

        assertNull(responseBody.get("data"));
        assertEquals("Booking deleted successfully", responseBody.get("message"));
    }


    @Test
    void testGetBookingDetailsByUserIdSuccess() {
        // Arrange
        Long userId = 1L;
        List<Map<String, Object>> bookingDetails = Collections.singletonList(Collections.emptyMap());

        when(IBookingService.getBookingDetailsByUserId(userId)).thenReturn(bookingDetails);

        // Act
        ResponseEntity<Object> responseEntity = bookingController.getBookingDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("Booking details retrieved successfully", responseBody.get("message"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetBookingDetailsByUserIdNoDetailsFound() {
        // Arrange
        Long userId = 1L;
        List<Map<String, Object>> emptyBookingDetails = Collections.emptyList();

        when(IBookingService.getBookingDetailsByUserId(userId)).thenReturn(emptyBookingDetails);

        // Act
        ResponseEntity<Object> responseEntity = bookingController.getBookingDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertEquals("No booking details found for the given user.", responseBody.get("error"));

        verify(logger, never()).error(anyString());
    }


}

