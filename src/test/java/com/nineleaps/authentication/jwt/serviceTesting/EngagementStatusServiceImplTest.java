package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.EngagementStatusDTO;
import com.nineleaps.authentication.jwt.entity.EngagementStatus;
import com.nineleaps.authentication.jwt.enums.EngStatus;
import com.nineleaps.authentication.jwt.repository.EngagementStatusRepository;
import com.nineleaps.authentication.jwt.service.implementation.EngagementServiceImpl;
import com.nineleaps.authentication.jwt.service.implementation.EngagementStatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class EngagementStatusServiceImplTest {

    @Mock
    private EngagementStatusRepository engagementStatusRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EngagementStatusServiceImpl engagementStatusServiceImpl;
    private static final Logger logger = LoggerFactory.getLogger(EngagementServiceImpl.class);


    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testCreateOrUpdateEngagementStatus_Success() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
        engagementStatusDTO.setEngagementId(engagementId);
        engagementStatusDTO.setMentorStatusTimestamp(LocalDateTime.now());
        engagementStatusDTO.setMenteeStatusTimestamp(LocalDateTime.now().plusHours(1));
        EngagementStatus existingStatus = new EngagementStatus();
        existingStatus.setMentorEngStatus(EngStatus.PENDING);
        existingStatus.setMenteeEngStatus(EngStatus.PENDING);

        EngagementStatusDTO expectedResult = new EngagementStatusDTO();

        when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(existingStatus));
        when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(new EngagementStatusDTO());

        // Act
        ResponseEntity<EngagementStatusDTO> response = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
     void testCreateOrUpdateEngagementStatus_Failure() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
        engagementStatusDTO.setEngagementId(engagementId);

        when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(new EngagementStatus()));
        when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(null);

        // Act
        ResponseEntity<EngagementStatusDTO> response = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

    }

     @Test
     void testProcessEngagementStatusWithExistingStatus() {
         // Arrange
         Long engagementId = 1L;
         EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
         engagementStatusDTO.setEngagementId(engagementId);

         engagementStatusDTO.setMentorEngStatus(EngStatus.PENDING);
         engagementStatusDTO.setMenteeEngStatus(EngStatus.PENDING);

         EngagementStatus existingEngagementStatus = new EngagementStatus();
         existingEngagementStatus.setMentorEngStatus(EngStatus.PENDING);
         existingEngagementStatus.setMenteeEngStatus(EngStatus.PENDING);
         existingEngagementStatus.setMentorStatusTimestamp(LocalDateTime.now().minusHours(1));
         existingEngagementStatus.setMenteeStatusTimestamp(LocalDateTime.now().minusHours(2));

         // Mock the behavior of the repository
         when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(existingEngagementStatus));

         // Mock the behavior of the modelMapper
         when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(engagementStatusDTO);

         // Act
         ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

         // Assert
         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         assertEquals(EngStatus.PENDING, responseEntity.getBody().getMentorEngStatus());
         assertEquals(EngStatus.PENDING, responseEntity.getBody().getMenteeEngStatus());

     }
     @Test
     void testProcessEngagementStatusWithNewStatus() {
         // Arrange
         Long engagementId = 1L;
         EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
         engagementStatusDTO.setEngagementId(engagementId);
         engagementStatusDTO.setMentorEngStatus(EngStatus.PENDING);
         engagementStatusDTO.setMenteeEngStatus(EngStatus.PENDING);

         // Mock the behavior of the repository to return an empty Optional, indicating no existing status
         when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.empty());

         // Mock the behavior of the modelMapper
         when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(engagementStatusDTO);

         // Act
         ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

         // Assert
         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         assertEquals(EngStatus.PENDING, responseEntity.getBody().getMentorEngStatus());
         assertEquals(EngStatus.PENDING, responseEntity.getBody().getMenteeEngStatus());
     }


     @Test
     void testProcessEngagementStatusWithCompletedStatus() {
         // Arrange
         Long engagementId = 1L;
         EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
         engagementStatusDTO.setEngagementId(engagementId);
         engagementStatusDTO.setMentorEngStatus(EngStatus.DONE);
         engagementStatusDTO.setMenteeEngStatus(EngStatus.DONE);
         engagementStatusDTO.setMentorStatusTimestamp(LocalDateTime.now().minusHours(1));
         engagementStatusDTO.setMenteeStatusTimestamp(LocalDateTime.now().minusHours(2));
         EngagementStatus existingEngagementStatus = new EngagementStatus();
         existingEngagementStatus.setMentorEngStatus(EngStatus.PENDING);
         existingEngagementStatus.setMenteeEngStatus(EngStatus.PENDING);
         existingEngagementStatus.setMentorStatusTimestamp(LocalDateTime.now().minusHours(3));
         existingEngagementStatus.setMenteeStatusTimestamp(LocalDateTime.now().minusHours(4));
         when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(existingEngagementStatus));
         when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(engagementStatusDTO);
         ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);
         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         assertEquals(EngStatus.DONE, responseEntity.getBody().getMentorEngStatus());
         assertEquals(EngStatus.DONE, responseEntity.getBody().getMenteeEngStatus());

     }

     @Test
     void testProcessEngagementStatusWithOneStatusNotDone() {
         // Arrange
         Long engagementId = 1L;
         EngagementStatusDTO engagementStatusDTO = new EngagementStatusDTO();
         engagementStatusDTO.setEngagementId(engagementId);

         // Set mentorEngStatus to DONE and menteeEngStatus to PENDING
         engagementStatusDTO.setMentorEngStatus(EngStatus.DONE);
         engagementStatusDTO.setMenteeEngStatus(EngStatus.PENDING);

         engagementStatusDTO.setMentorStatusTimestamp(LocalDateTime.now().minusHours(1));
         engagementStatusDTO.setMenteeStatusTimestamp(LocalDateTime.now().minusHours(2));

         EngagementStatus existingEngagementStatus = new EngagementStatus();
         existingEngagementStatus.setMentorEngStatus(EngStatus.DONE);
         existingEngagementStatus.setMenteeEngStatus(EngStatus.PENDING);
         existingEngagementStatus.setMentorStatusTimestamp(LocalDateTime.now().minusHours(3));
         existingEngagementStatus.setMenteeStatusTimestamp(LocalDateTime.now().minusHours(4));

         // Mock the behavior of the repository
         when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(existingEngagementStatus));

         // Mock the behavior of the modelMapper
         when(modelMapper.map(any(), eq(EngagementStatusDTO.class))).thenReturn(engagementStatusDTO);

         // Act
         ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

         // Assert
         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
         assertEquals(EngStatus.DONE, responseEntity.getBody().getMentorEngStatus());
         assertEquals(EngStatus.PENDING, responseEntity.getBody().getMenteeEngStatus());
         assertNull(responseEntity.getBody().getCompletedEngStatusTimestamp());

     }

    @Test
    void testGetEngagementStatusByEngagementId_ExistingEngagementStatus() {
        // Arrange
        Long engagementId = 1L;
        EngagementStatus engagementStatus = new EngagementStatus();

        // Mock the behavior of engagementStatusRepository
        when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.of(engagementStatus));

        // Mock the behavior of modelMapper.map
        when(modelMapper.map(engagementStatus, EngagementStatusDTO.class)).thenReturn(new EngagementStatusDTO());

        // Act
        ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.getEngagementStatusByEngagementId(engagementId);
        logger.info("Value of response entity is  {}",responseEntity );
        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }


    @Test
    void testGetEngagementStatusByEngagementId_NonExistingEngagementStatus() {
        // Arrange
        Long engagementId = 1L;

        when(engagementStatusRepository.findByEngagementId(engagementId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EngagementStatusDTO> responseEntity = engagementStatusServiceImpl.getEngagementStatusByEngagementId(engagementId);

        // Assert
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull(responseEntity.getBody());
    }

}
