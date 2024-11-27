package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.UserDTO;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.ConnectionRequestServiceImpl;
import com.nineleaps.authentication.jwt.service.interfaces.IConnectionRequestService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/v1/connectionRequests")
public class ConnectionRequestController {


    private final ConnectionRequestServiceImpl connectionRequestServiceImpl;
    private final IConnectionRequestService iConnectionRequestService;
    private static final Logger logger = LoggerFactory.getLogger(ConnectionRequestController.class);

    /**
     * Endpoint for a mentee to send a connection request to a chosen mentor.
     *
     * @param mentorId The ID of the mentor to whom the connection request is sent.
     * @param menteeId The ID of the mentee sending the connection request.
     * @param message  Additional message included in the connection request.
     * @return A response entity indicating the result of the connection request.
     */
    @PostMapping("/sendConnection")
    @PreAuthorize("hasAnyAuthority('MENTEE')")
    @ApiOperation("Send a Connection Request to your chosen mentor")
    public ResponseEntity<Object> sendConnectionRequest(
            @RequestParam Long mentorId, @RequestParam Long menteeId, @RequestParam String message) {
        // Send a connection request from mentee to mentor
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.sendConnectionRequest(mentorId, menteeId, message);

        if (connectionRequestDto != null) {
            logger.info("Connection request sent successfully. Mentor ID: {}, Mentee ID: {}", mentorId, menteeId);

            return ResponseHandler.success("Connection request sent successfully.", HttpStatus.OK, connectionRequestDto);
        } else {
            logger.error("Failed to send connection request. Mentor ID: {}, Mentee ID: {}", mentorId, menteeId);

            return ResponseHandler.error("Failed to send connection request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for accepting a connection request sent by a mentee.
     *
     * @param connectionRequestId The ID of the connection request to be accepted.
     * @param mentorId            The ID of the mentor accepting the connection request.
     * @param menteeId            The ID of the mentee who sent the connection request.
     * @return A response entity indicating the result of the acceptance operation.
     */
    @PutMapping("/accept")
//    @PreAuthorize("hasAnyAuthority('MENTOR')")
    @ApiOperation("Accept a connection Request sent by Mentee")
    public ResponseEntity<Object> acceptConnectionRequest(
            @RequestParam("id") Long connectionRequestId,
            @RequestParam("mentorId") Long mentorId,
            @RequestParam("menteeId") Long menteeId) {
        // Accept a connection request by the mentor
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.acceptConnectionRequest(connectionRequestId, mentorId, menteeId);

        if (connectionRequestDto != null) {
            logger.info("Connection request accepted successfully. Connection Request ID: {}", connectionRequestId);
            return ResponseHandler.success("Connection request accepted successfully.", HttpStatus.OK, connectionRequestDto);
        } else {
            logger.error("Failed to accept connection request. Connection Request ID: {}", connectionRequestId);
            return ResponseHandler.error("Failed to accept connection request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for rejecting a connection request sent by a mentee.
     *
     * @param connectionRequestId The ID of the connection request to be rejected.
     * @param mentorId            The ID of the mentor rejecting the connection request.
     * @param menteeId            The ID of the mentee who sent the connection request.
     * @return A response entity indicating the result of the rejection operation.
     */
    @PutMapping("/reject")
//    @PreAuthorize("hasAnyAuthority('MENTOR')")
    @ApiOperation("Reject a connection Request sent by Mentor")
    public ResponseEntity<Object> rejectConnectionRequest(
            @RequestParam("id") Long connectionRequestId,
            @RequestParam("mentorId") Long mentorId,
            @RequestParam("menteeId") Long menteeId) {
        // Reject a connection request by the mentor
        ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.rejectConnectionRequest(connectionRequestId, mentorId, menteeId);

        if (connectionRequestDto != null) {
            logger.info("Connection request rejected successfully. Connection Request ID: {}", connectionRequestId);
            return ResponseHandler.success("Connection request rejected successfully.", HttpStatus.OK, connectionRequestDto);
        } else {
            logger.error("Failed to reject connection request. Connection Request ID: {}", connectionRequestId);
            return ResponseHandler.error("Failed to reject connection request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving users with accepted connections and average ratings for a user.
     *
     * @param userId The ID of the user for whom to retrieve accepted connections.
     * @return A response entity containing the users with accepted connections and average ratings.
     */
    @GetMapping("/acceptedConnectionsByUserId")
//    @PreAuthorize("hasAnyAuthority('MENTEE','MENTOR')")
    @ApiOperation("Get Users with Accepted Connection By User Id")
    public ResponseEntity<Object> getUsersWithAcceptedConnection(@RequestParam Long userId) {
        // Retrieve users with accepted connections and average ratings for a user
        List<UserDTO> usersWithAcceptedConnection = connectionRequestServiceImpl.getUsersWithAcceptedConnectionAndAverageRating(userId);

        if (!usersWithAcceptedConnection.isEmpty()) {
            logger.info("Retrieved users with accepted connections for User ID: {}", userId);
            return ResponseHandler.success("Users with accepted connections retrieved successfully.", HttpStatus.OK, usersWithAcceptedConnection);
        } else {
            logger.error("No users with accepted connections found for User ID: {}", userId);
            return ResponseHandler.error("No users with accepted connections found.", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Get all connection requests for a user with pagination.
     *
     * @param userId The ID of the user for whom to retrieve connection requests.
     * @param page   The page number for pagination.
     * @param size   The number of items per page.
     * @return List of connection requests with pagination.
     */
    @GetMapping("/connections/{userId}/all")
    @ApiOperation("Get all connection Requests")
//    @PreAuthorize("hasAnyAuthority('MENTEE','MENTOR')")
    public ResponseEntity<List<ConnectionRequestDto>> getAllConnectionRequestsByUserId(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        // Retrieve all connection requests by user ID with pagination
        List<ConnectionRequestDto> connectionRequests = iConnectionRequestService.getAllConnectionRequestsByUserId(userId, page, size);
        return new ResponseEntity<>(connectionRequests, HttpStatus.OK);
    }

    /**
     * Endpoint for searching connection requests by mentee ID.
     *
     * @param menteeId The ID of the mentee for whom to search connection requests.
     * @return A response entity containing the searched connections.
     */
    @GetMapping("/searchTheConnectionByMentorId")
//    @PreAuthorize("hasAnyAuthority('MENTEE','MENTOR')")
    @ApiOperation("Search for a connection Request by MenteeId")
    public ResponseEntity<Object> searchByConnectionId(@RequestParam("mentorId") Long menteeId) {
        // Search for connection requests by mentee ID
        List<Object[]> connections = connectionRequestServiceImpl.findConnectionsByMenteeId(menteeId);

        if (!connections.isEmpty()) {
            logger.info("Searched connections by Mentee ID: {}", menteeId);
            return ResponseHandler.success("Connections retrieved successfully.", HttpStatus.OK, connections);
        } else {
            logger.info("No connections found for Mentee ID: {}", menteeId);
            return ResponseHandler.error("No connections found.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for recommending mentors for a connection request.
     *
     * @param connectionRequestId    The ID of the connection request for which mentors are recommended.
     * @param recommendedMentorIds   List of mentor IDs recommended for the connection request.
     * @return A response entity indicating the result of the mentor recommendation operation.
     */
    @PutMapping("/recommendMentors")
    @ApiOperation("Recommending the mentors for a connection request")
    public ResponseEntity<Object> recommendMentors(
            @RequestParam Long connectionRequestId,
            @RequestParam List<Long> recommendedMentorIds) {
        try {
            // Recommend mentors for a connection request
            ConnectionRequestDto connectionRequestDto = connectionRequestServiceImpl.recommendMentors(connectionRequestId, recommendedMentorIds);
            return ResponseHandler.success("Mentor recommendations added successfully.", HttpStatus.OK, connectionRequestDto);
        } catch (ResourceNotFoundException ex) {
            logger.error("Error recommending mentors for Connection Request ID: {}", connectionRequestId);
            return ResponseHandler.error("Error recommending mentors: " + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/status")
    @ApiOperation("Get the connection status between mentee and mentor")
    public ResponseEntity<Object> getConnectionStatus(@RequestParam Long menteeId, @RequestParam Long mentorId) {
        ConnectionRequestStatus connectionStatus = connectionRequestServiceImpl.getConnectionStatus(menteeId, mentorId);

        if (connectionStatus != null) {
            logger.info("Connection status retrieved successfully. Mentee ID: {}, Mentor ID: {}", menteeId, mentorId);
            return ResponseHandler.success("Connection status retrieved successfully.", HttpStatus.OK, connectionStatus.toString());
        } else {
            logger.error("Connection status not found. Mentee ID: {}, Mentor ID: {}", menteeId, mentorId);
            return ResponseHandler.error("Connection status not found.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving details of pending connections by user ID.
     *
     * @param userId The ID of the user for whom to retrieve pending connections.
     * @return A response entity containing the details of pending connections.
     */

    @GetMapping("/pendingConnectionByUserId")
    @ApiOperation("Get details of pending connections by user ID")
    public ResponseEntity<Object> getConnectionDetailsByUserId(@RequestParam("userId") Long userId) {
        List<Map<String, Object>> connectionDetails = connectionRequestServiceImpl.getConnectionDetailsByUserId(userId);

        if (connectionDetails == null) {
            logger.error("No pending connections found for User ID: {}", userId);
            return ResponseHandler.error("No pending connections found for the given user ID.", HttpStatus.NOT_FOUND);

        } else {
            logger.info("Pending connections retrieved successfully for User ID: {}", userId);
            return ResponseHandler.success("Pending connections retrieved successfully.", HttpStatus.OK, connectionDetails);
        }
    }

    /**
     * Endpoint for retrieving all recommended mentors for a mentee.
     *
     * @param menteeId The ID of the mentee for whom to retrieve recommended mentors.
     * @return A response entity containing the list of recommended mentors.
     */
    @GetMapping("/mentee/recommendedMentors")
    @ApiOperation("Get all recommended mentors for a mentee")
    public ResponseEntity<List<UserDTO>> getRecommendedMentors(@RequestParam Long menteeId) {
        List<UserDTO> recommendedMentors = connectionRequestServiceImpl.getRecommendedMentorsByMenteeId(menteeId);
        return ResponseEntity.ok(recommendedMentors);
    }
}
