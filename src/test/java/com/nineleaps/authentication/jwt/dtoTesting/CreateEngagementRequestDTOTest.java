package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

 class CreateEngagementRequestDTOTest {

    @Test
    void testCreateEngagementRequestDTOConstructorAndGetters() {
        LocalDateTime startTime = LocalDateTime.now();

        ConnectionRequestDto connectionRequest = new ConnectionRequestDto();
        connectionRequest.setId(1L);
        connectionRequest.setMenteeId(201L);
        connectionRequest.setMentorId(301L);

        CreateEngagementRequestDTO createEngagementRequestDTO = new CreateEngagementRequestDTO();
        createEngagementRequestDTO.setId(1L);
        createEngagementRequestDTO.setConnectionRequest(connectionRequest);
        createEngagementRequestDTO.setStartTime(startTime.toString());
        createEngagementRequestDTO.setDurationHours(2);

        assertEquals(1L, createEngagementRequestDTO.getId());
        assertEquals(connectionRequest, createEngagementRequestDTO.getConnectionRequest());
        assertEquals(startTime, createEngagementRequestDTO.getStartTime());
        assertEquals(2, createEngagementRequestDTO.getDurationHours());
    }

    @Test
    void testCreateEngagementRequestDTONoArgsConstructor() {
        CreateEngagementRequestDTO createEngagementRequestDTO = new CreateEngagementRequestDTO();

        assertNotNull(createEngagementRequestDTO);
        assertNull(createEngagementRequestDTO.getId());
        assertNull(createEngagementRequestDTO.getConnectionRequest());
        assertNull(createEngagementRequestDTO.getStartTime());
        assertEquals(0, createEngagementRequestDTO.getDurationHours());
    }

    @Test
    void testCreateEngagementRequestDTOSetters() {
        CreateEngagementRequestDTO createEngagementRequestDTO = new CreateEngagementRequestDTO();

        LocalDateTime startTime = LocalDateTime.now().plusHours(1);

        ConnectionRequestDto connectionRequest = new ConnectionRequestDto();
        connectionRequest.setId(2L);
        connectionRequest.setMenteeId(202L);
        connectionRequest.setMentorId(302L);

        createEngagementRequestDTO.setId(2L);
        createEngagementRequestDTO.setConnectionRequest(connectionRequest);
        createEngagementRequestDTO.setStartTime(startTime.toString());
        createEngagementRequestDTO.setDurationHours(3);

        assertEquals(2L, createEngagementRequestDTO.getId());
        assertEquals(connectionRequest, createEngagementRequestDTO.getConnectionRequest());
        assertEquals(startTime, createEngagementRequestDTO.getStartTime());
        assertEquals(3, createEngagementRequestDTO.getDurationHours());
    }


     @Test
     void testFromEngagement() {
         LocalDateTime startTime = LocalDateTime.now();
         CreateEngagementRequestDTO dto = new CreateEngagementRequestDTO();
         dto.setId(1L);
         dto.setStartTime(startTime.toString());
         dto.setDurationHours(2);
         ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
         connectionRequestDto.setId(101L);
         connectionRequestDto.setMenteeId(201L);

         assertAll("DTO properties mapping",
                 () -> assertEquals(1L, dto.getId()),
                 () -> assertEquals(2, dto.getDurationHours())
         );
     }




     @Test
        void testGetStartTimeAndSetStartTime() {
            CreateEngagementRequestDTO dto = new CreateEngagementRequestDTO();
            LocalDateTime startTime = LocalDateTime.now();
            String startTimeString = startTime.toString();
            dto.setStartTime(startTimeString);

            assertEquals(startTime, dto.getStartTime());

            LocalDateTime newStartTime = LocalDateTime.now().plusHours(1);
            String newStartTimeString = newStartTime.toString();
            dto.setStartTime(newStartTimeString);
            assertEquals(newStartTime, dto.getStartTime());
        }

        @Test
        void testToEngagement() {
            CreateEngagementRequestDTO dto = new CreateEngagementRequestDTO();
            LocalDateTime startTime = LocalDateTime.now();
            dto.setStartTime(startTime.toString());
            dto.setDurationHours(2);

            Engagement engagement = dto.toEngagement();

            assertEquals(startTime, engagement.getStartTime());
            assertEquals(dto.getDurationHours(), engagement.getDurationHours());
        }


}
