package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class ConnectionRequestDtoTest {

    @Test
    void testConnectionRequestDtoConstructorAndGetters() {
        LocalDateTime requestTime = LocalDateTime.now();
        LocalDateTime acceptanceTime = LocalDateTime.now();
        LocalDateTime rejectionTime = LocalDateTime.now();

        List<Long> recommendedMentors = new ArrayList<>();
        recommendedMentors.add(101L);
        recommendedMentors.add(102L);
        User mentee=new User();
        mentee.setId(1L);
        User mentor =new User();
        mentor.setId(2L);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(1L);
        connectionRequest.setMentor(mentor);
        connectionRequest.setMentee(mentee);

        connectionRequest.setMessage("Request message");
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        connectionRequest.setRequestTime(requestTime);
        connectionRequest.setAcceptanceTime(acceptanceTime);
        connectionRequest.setRejectionTime(rejectionTime);

        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto(connectionRequest);

        assertEquals(1L, connectionRequestDto.getId());
        assertEquals(1L, connectionRequestDto.getMenteeId());
        assertEquals(2L, connectionRequestDto.getMentorId());
        assertEquals("Request message", connectionRequestDto.getMessage());
        assertEquals(ConnectionRequestStatus.PENDING, connectionRequestDto.getStatus());
        assertEquals(requestTime, connectionRequestDto.getRequestTime());
        assertEquals(acceptanceTime, connectionRequestDto.getAcceptanceTime());
        assertEquals(rejectionTime, connectionRequestDto.getRejectionTime());
    }


    @Test
    void testConnectionRequestDtoFromConnectionRequest() {
        LocalDateTime requestTime = LocalDateTime.now();
        LocalDateTime acceptanceTime = LocalDateTime.now();
        LocalDateTime rejectionTime = LocalDateTime.now();

        List<Long> recommendedMentors = new ArrayList<>();
        recommendedMentors.add(101L);
        recommendedMentors.add(102L);
        User mentee=new User();
        mentee.setId(1L);
        User mentor =new User();
        mentor.setId(2L);

        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(1L);
        connectionRequest.setMentor(mentor);
        connectionRequest.setMentee(mentee);

        connectionRequest.setMessage("Request message");
        connectionRequest.setStatus(ConnectionRequestStatus.PENDING);
        connectionRequest.setRequestTime(requestTime);
        connectionRequest.setAcceptanceTime(acceptanceTime);
        connectionRequest.setRejectionTime(rejectionTime);
        ConnectionRequestDto connectionRequestDto = ConnectionRequestDto.fromConnectionRequest(connectionRequest);

        assertEquals(1L, connectionRequestDto.getId());

        assertEquals("Request message", connectionRequestDto.getMessage());
        assertEquals(ConnectionRequestStatus.PENDING, connectionRequestDto.getStatus());
        assertEquals(requestTime, connectionRequestDto.getRequestTime());
        assertEquals(acceptanceTime, connectionRequestDto.getAcceptanceTime());
        assertEquals(rejectionTime, connectionRequestDto.getRejectionTime());
    }
}
