package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.ConnectionRequestController;
import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.UserDTO;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import com.nineleaps.authentication.jwt.service.implementation.ConnectionRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConnectionRequestControllerTest {

    @InjectMocks
    private ConnectionRequestController connectionRequestController;

    @Mock
    private ConnectionRequestServiceImpl connectionRequestServiceImpl;

    @Mock
    private Logger logger;

    @Test
    void testSendConnectionRequestSuccess() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Hello, mentor!";

        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(1L);
        connectionRequestDto.setMentorId(mentorId);
        connectionRequestDto.setMenteeId(menteeId);
        connectionRequestDto.setMessage(message);

        when(connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message)).thenReturn(connectionRequestDto);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.sendConnectionRequest(mentorId, menteeId, message);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Connection request sent successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));


    }

    @Test
    void testSendConnectionRequestFailure() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        String message = "Hello, mentor!";

        when(connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.sendConnectionRequest(mentorId, menteeId, message);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to send connection request.", responseBody.get("error"));
        assertNull(responseBody.get("data"));

    }

    @Test
    void testAcceptConnectionRequestSuccess() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequestId);
        connectionRequestDto.setMentorId(mentorId);
        connectionRequestDto.setMenteeId(menteeId);

        when(connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId)).thenReturn(connectionRequestDto);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        // Check the message and data
        assertEquals("Connection request accepted successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));
        assertEquals(connectionRequestDto, responseBody.get("data"));


    }

    @Test
    void testAcceptConnectionRequestFailure() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        when(connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);
        // Check the error message and data (should be null)
        assertEquals("Failed to accept connection request.", responseBody.get("error"));
        assertNull(responseBody.get("data"));

    }

    @Test
    void testRejectConnectionRequestSuccess() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequestId);
        connectionRequestDto.setMentorId(mentorId);
        connectionRequestDto.setMenteeId(menteeId);

        when(connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId)).thenReturn(connectionRequestDto);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the message and data
        assertEquals("Connection request rejected successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));


    }

    @Test
    void testRejectConnectionRequestFailure() {
        // Arrange
        Long connectionRequestId = 1L;
        Long mentorId = 2L;
        Long menteeId = 3L;

        when(connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the error message and data (should be null)
        assertEquals("Failed to reject connection request.", responseBody.get("error"));
        assertNull(responseBody.get("data"));

    }


    @Test
    void testGetUsersWithAcceptedConnectionSuccess() {
        // Arrange
        Long userId = 1L;
        List<UserDTO> usersWithAcceptedConnection = new ArrayList<>();
        usersWithAcceptedConnection.add(new UserDTO());

        when(connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(userId)).thenReturn(usersWithAcceptedConnection);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getUsersWithAcceptedConnection(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the message and data
        assertEquals("Users with accepted connections retrieved successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));


        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetUsersWithAcceptedConnectionNoUsersFound() {
        // Arrange
        Long userId = 1L;
        List<UserDTO> emptyUsersWithAcceptedConnection = new ArrayList<>();

        when(connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(userId)).thenReturn(emptyUsersWithAcceptedConnection);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getUsersWithAcceptedConnection(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        // Check the error message and data (should be null)
        assertEquals("No users with accepted connections found.", responseBody.get("error"));
        assertNull(responseBody.get("data"));


        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetAllConnectionRequestsByUserId() {
        // Arrange
        Long userId = 1L;
        int page = 0;
        int size = 10; // Set the appropriate values for page and size
        List<ConnectionRequestDto> connectionRequestDtoList = new ArrayList<>();

        when(connectionRequestServiceImpl.getAllConnectionRequestsByUserId(userId, page, size)).thenReturn(connectionRequestDtoList);
        ResponseEntity<List<ConnectionRequestDto>> response = connectionRequestController.getAllConnectionRequestsByUserId(userId, page, size);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(connectionRequestDtoList, response.getBody());
    }


    @Test
    void testSearchByConnectionIdSuccess() {
        // Arrange
        Long menteeId = 1L;
        List<Object[]> connections = new ArrayList<>();
        connections.add(new Object[]{1L, "Connection 1"});

        when(connectionRequestServiceImpl.findConnectionsByMenteeId(menteeId)).thenReturn(connections);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.searchByConnectionId(menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Connections retrieved successfully.", responseBody.get("message"));
        assertNotNull(responseBody.get("data"));


        verify(logger, never()).error(anyString());
    }

    @Test
    void testSearchByConnectionIdNoConnectionsFound() {
        // Arrange
        Long menteeId = 1L;
        List<Object[]> emptyConnections = new ArrayList<>();

        when(connectionRequestServiceImpl.findConnectionsByMenteeId(menteeId)).thenReturn(emptyConnections);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.searchByConnectionId(menteeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No connections found.", responseBody.get("error"));
        assertNull(responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testRecommendMentorsSuccess() throws ResourceNotFoundException {
        // Arrange
        Long connectionRequestId = 1L;
        List<Long> recommendedMentorIds = Arrays.asList(2L, 3L);
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();

        when(connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds)).thenReturn(connectionRequestDto);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.recommendMentors(connectionRequestId, recommendedMentorIds);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Mentor recommendations added successfully.", responseBody.get("message"));
        assertEquals(connectionRequestDto, responseBody.get("data"));


        verify(logger, never()).error(anyString());
    }

    @Test
    void testRecommendMentorsNotFound() throws ResourceNotFoundException {
        // Arrange
        Long connectionRequestId = 1L;
        List<Long> recommendedMentorIds = Arrays.asList(2L, 3L);

        when(connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds)).thenThrow(ResourceNotFoundException.class);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.recommendMentors(connectionRequestId, recommendedMentorIds);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertTrue(responseBody.get("error").toString().contains("Error recommending mentors"));


        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetConnectionStatusSuccess() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        ConnectionRequestStatus connectionStatus = ConnectionRequestStatus.ACCEPTED;

        when(connectionRequestServiceImpl.getConnectionStatus(menteeId, mentorId)).thenReturn(connectionStatus);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getConnectionStatus(menteeId, mentorId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Connection status retrieved successfully.", responseBody.get("message"));
        assertEquals(connectionStatus.toString(), responseBody.get("data"));


        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetConnectionStatusNotFound() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;

        when(connectionRequestServiceImpl.getConnectionStatus(menteeId, mentorId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getConnectionStatus(menteeId, mentorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertTrue(responseBody.get("error").toString().contains("Connection status not found"));

        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetConnectionDetailsByUserIdSuccess() {
        // Arrange
        Long userId = 1L;

        // Create some mock connection details data
        List<Map<String, Object>> mockConnectionDetails = new ArrayList<>();
        Map<String, Object> connection1 = new HashMap<>();
        connection1.put("menteeName", "Mentee1");
        connection1.put("mentorName", "Mentor1");
        connection1.put("menteeId", 101L);
        connection1.put("mentorId", 201L);
        connection1.put("message", "Hello from Mentee1");
        mockConnectionDetails.add(connection1);

        when(connectionRequestServiceImpl.getConnectionDetailsByUserId(userId)).thenReturn(mockConnectionDetails);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getConnectionDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Pending connections retrieved successfully.", responseBody.get("message"));
        assertEquals(mockConnectionDetails, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testGetConnectionDetailsByUserIdNotFound() {
        // Arrange
        Long userId = 1L;

        when(connectionRequestServiceImpl.getConnectionDetailsByUserId(userId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = connectionRequestController.getConnectionDetailsByUserId(userId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertTrue(responseBody.get("error").toString().contains("No pending connections found"));


        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetRecommendedMentors() {
        // Arrange
        Long menteeId = 1L;
        List<UserDTO> recommendedMentors = new ArrayList<>();

        when(connectionRequestServiceImpl.getRecommendedMentorsByMenteeId(menteeId)).thenReturn(recommendedMentors);

        // Act
        ResponseEntity<List<UserDTO>> response = connectionRequestController.getRecommendedMentors(menteeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(recommendedMentors, response.getBody());
    }
}
