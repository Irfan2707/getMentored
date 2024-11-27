package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestDto;
import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.DuplicateEngagementException;
import com.nineleaps.authentication.jwt.repository.ConnectionRequestRepo;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.service.implementation.EmailServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.EngagementServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.SearchForMentorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
 class EngagementServiceImplTest {

    @InjectMocks
    private EngagementServiceImpl engagementServiceImpl;

    @Mock
    private EngagementRepository engagementRepository;

    @Mock
    private ConnectionRequestRepo connectionRequestRepository;

    @Mock
    private EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(SearchForMentorServiceImpl.class);
    @Test
    void testCreateEngagement_Success() {
        // Arrange
        Long connectionRequestId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        int durationHours = 2;
        User mentee=new User();
        mentee.setId(1L);
        User mentor=new User();
        mentor.setId(2L);
        mentor.setUserName("mentor");

        // Create a mock ConnectionRequest object and Engagement object
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.setId(connectionRequestId);
        connectionRequest.setMentee(mentee);
        connectionRequest.setMentor(mentor);
        Engagement engagement = new Engagement();
        engagement.setId(1L);
        engagement.setConnectionRequest(connectionRequest);
        engagement.setStartTime(LocalDateTime.now());

        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO();
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequestId);
        requestDTO.setConnectionRequest(connectionRequestDto);
        requestDTO.setStartTime(String.valueOf(startTime));
        requestDTO.setDurationHours(durationHours);

        when(connectionRequestRepository.findById(connectionRequestId)).thenReturn(Optional.of(connectionRequest));
        when(engagementRepository.existsByConnectionRequestId(connectionRequestId)).thenReturn(false);
        when(engagementRepository.save(any(Engagement.class))).thenReturn(engagement);

        // Act
        ResponseEntity<CreateEngagementRequestDTO> responseEntity = engagementServiceImpl.createEngagement(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        verify(connectionRequestRepository, times(1)).findById(connectionRequestId);
        verify(engagementRepository, times(1)).existsByConnectionRequestId(connectionRequestId);
        verify(engagementRepository, times(1)).save(any(Engagement.class));
        verify(emailService, times(1)).sendEmail(
                "Engagement Confirmation",
                "Your Mentor mentor has carefully evaluated the goals and objectives, and the time required to cover the relevant topics. The estimated duration of 2 hours will allow for comprehensive guidance and support so that you can make the most of this mentorship opportunity. \n Happy Learning!!!! ",
                null
        );    }



    @Test
    void testCreateEngagement_DuplicateEngagementException() {
        // Arrange
        Long connectionRequestId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        int durationHours = 2;

        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO();
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequestId);
        requestDTO.setConnectionRequest(connectionRequestDto);
        requestDTO.setStartTime(String.valueOf(startTime));
        requestDTO.setDurationHours(durationHours);

        when(connectionRequestRepository.findById(connectionRequestId)).thenReturn(Optional.of(new ConnectionRequest()));
        when(engagementRepository.existsByConnectionRequestId(connectionRequestId)).thenReturn(true);

        assertThrows(DuplicateEngagementException.class, () -> engagementServiceImpl.createEngagement(requestDTO));
        verify(connectionRequestRepository, times(1)).findById(connectionRequestId);
        verify(engagementRepository, times(1)).existsByConnectionRequestId(connectionRequestId);
        verify(engagementRepository, never()).save(any(Engagement.class));
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testCreateEngagement_ConnectionRequestNotFound() {
        // Arrange
        Long connectionRequestId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        int durationHours = 2;

        CreateEngagementRequestDTO requestDTO = new CreateEngagementRequestDTO();
        ConnectionRequestDto connectionRequestDto = new ConnectionRequestDto();
        connectionRequestDto.setId(connectionRequestId);
        requestDTO.setConnectionRequest(connectionRequestDto);
        requestDTO.setStartTime(String.valueOf(startTime));
        requestDTO.setDurationHours(durationHours);

        when(connectionRequestRepository.findById(connectionRequestId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> engagementServiceImpl.createEngagement(requestDTO));
        verify(connectionRequestRepository, times(1)).findById(connectionRequestId);
        verify(engagementRepository, never()).existsByConnectionRequestId(connectionRequestId);
        verify(engagementRepository, never()).save(any(Engagement.class));
        verify(emailService, never()).sendEmail(any(), any(), any());
    }


    @Test
    void testGetEngagementDetailsByUserId() {
        // Arrange
        Long userId = 1L;
        List<Map<String, Object>> expectedResult = new ArrayList<>();

        when(engagementRepository.findEngagementDetailsByUserId(userId)).thenReturn(expectedResult);

        // Act
        List<Map<String, Object>> result = engagementServiceImpl.getEngagementDetailsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedResult.size(), result.size());

        verify(engagementRepository, times(1)).findEngagementDetailsByUserId(userId);
    }

}
