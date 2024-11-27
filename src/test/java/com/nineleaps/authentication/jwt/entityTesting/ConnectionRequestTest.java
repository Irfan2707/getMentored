package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConnectionRequestTest {

    @Test
    void testValidation() {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        Long id = 1L;
        User mentee = new User();
        User mentor = new User();
        String message = "Test Connection Request";
        ConnectionRequestStatus status = ConnectionRequestStatus.PENDING;
        LocalDateTime requestTime = LocalDateTime.now();
        LocalDateTime acceptanceTime = LocalDateTime.now();
        LocalDateTime rejectionTime = LocalDateTime.now();
        List<Long> recommendedMentors = new ArrayList<>();

        connectionRequest.setId(id);
        connectionRequest.setMentee(mentee);
        connectionRequest.setMentor(mentor);
        connectionRequest.setMessage(message);
        connectionRequest.setStatus(status);
        connectionRequest.setRequestTime(requestTime);
        connectionRequest.setAcceptanceTime(acceptanceTime);
        connectionRequest.setRejectionTime(rejectionTime);
        connectionRequest.setRecommendedMentors(recommendedMentors);

        assertEquals(ConnectionRequestStatus.PENDING, connectionRequest.getStatus());
        assertEquals(message,connectionRequest.getMessage());


    }
    // Test the NoArgsConstructor
    @Test
    void testNoArgsConstructor() {

        ConnectionRequest connectionRequest = new ConnectionRequest();

        assertNull(connectionRequest.getId());
        assertNull(connectionRequest.getMentee());
        assertNull(connectionRequest.getMentor());
        assertNull(connectionRequest.getMessage());
        assertNull(connectionRequest.getStatus());
        assertNull(connectionRequest.getRequestTime());
        assertNull(connectionRequest.getAcceptanceTime());
        assertNull(connectionRequest.getRejectionTime());
        assertNull(connectionRequest.getRecommendedMentors());
    }
}
