package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.exception.DuplicateResourceException;
import com.nineleaps.authentication.jwt.repository.GoalTrackerRepository;
import com.nineleaps.authentication.jwt.service.implementation.GoalTrackerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


 class GoalTrackerServiceImplTest {

    @InjectMocks
    private GoalTrackerServiceImpl goalTrackerService;

    @Mock
    private GoalTrackerRepository goalTrackerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private Logger logger;

    @BeforeEach
     void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testCreateGoalTracker_Success() throws DuplicateResourceException {

        // Arrange
        String description="Test Goal Tracker";
        GoalTrackerDTO goalTrackerDto = new GoalTrackerDTO();
        goalTrackerDto.setDescription(description);
        goalTrackerDto.setEngagementId(1L);
        goalTrackerDto.setId(1L);
        goalTrackerDto.setGoalTrackerStartTime(LocalDateTime.now());

        GoalTracker goalTrackerEntity = new GoalTracker();
        goalTrackerEntity.setId(1L);

        when(modelMapper.map(goalTrackerDto, GoalTracker.class)).thenReturn(goalTrackerEntity);
        when(goalTrackerRepository.existsByDescriptionAndEngagementId(description, 1L)).thenReturn(false);
        when(goalTrackerRepository.save(goalTrackerEntity)).thenReturn(goalTrackerEntity);
        when(modelMapper.map(goalTrackerEntity, GoalTrackerDTO.class)).thenReturn(goalTrackerDto);

        // Act
        GoalTrackerDTO createdGoalTracker = goalTrackerService.createGoalTracker(goalTrackerDto);

        // Assert
        assertNotNull(createdGoalTracker);
        assertEquals(1L, createdGoalTracker.getId());
        assertEquals("Test Goal Tracker", createdGoalTracker.getDescription());
        assertEquals(1L, createdGoalTracker.getEngagementId());
        assertNotNull(createdGoalTracker.getGoalTrackerStartTime());
        verify(goalTrackerRepository, times(1)).save(goalTrackerEntity);
    }

    @Test
     void testCreateGoalTracker_DuplicateResourceException() {
        // Arrange
        String description="Test Goal Tracker";
        GoalTrackerDTO goalTrackerDto = new GoalTrackerDTO();
        goalTrackerDto.setDescription(description);
        goalTrackerDto.setEngagementId(1L);

        when(goalTrackerRepository.existsByDescriptionAndEngagementId(description, 1L)).thenReturn(true);

        // Act and Assert
        assertThrows(DuplicateResourceException.class, () -> {
            goalTrackerService.createGoalTracker(goalTrackerDto);
        });
    }


    @Test
     void testUpdateGoalTracker_Success() throws ResourceNotFoundException {
        // Arrange
        Long goalTrackerId = 1L;

        GoalTrackerDTO goalTrackerDto = new GoalTrackerDTO();
        goalTrackerDto.setId(goalTrackerId);
        goalTrackerDto.setDescription("Updated Goal Tracker");

        GoalTracker goalTrackerEntity = new GoalTracker();
        goalTrackerEntity.setId(goalTrackerId);
        goalTrackerEntity.setId(goalTrackerId);

        when(modelMapper.map(goalTrackerDto, GoalTracker.class)).thenReturn(goalTrackerEntity);
        when(goalTrackerRepository.save(goalTrackerEntity)).thenReturn(goalTrackerEntity);
        when(modelMapper.map(goalTrackerEntity, GoalTrackerDTO.class)).thenReturn(goalTrackerDto);

        // Act
        GoalTrackerDTO updatedGoalTracker = goalTrackerService.updateGoalTracker(goalTrackerId, goalTrackerDto);

        // Assert
        assertNotNull(updatedGoalTracker);
        assertEquals(goalTrackerId, updatedGoalTracker.getId());
        assertEquals("Updated Goal Tracker", updatedGoalTracker.getDescription());
        verify(goalTrackerRepository, times(1)).save(goalTrackerEntity);
    }




    @Test
    void testDeleteGoalTracker_Success() throws ResourceNotFoundException {
       // Arrange
       Long goalTrackerId = 1L;

       GoalTracker goalTrackerToDelete = new GoalTracker();
       goalTrackerToDelete.setId(goalTrackerId);

       when(goalTrackerRepository.findById(goalTrackerId)).thenReturn(Optional.of(goalTrackerToDelete));

       // Act
       goalTrackerService.deleteGoalTracker(goalTrackerId);

       // Assert
       verify(goalTrackerRepository, times(1)).save(goalTrackerToDelete);
       assertTrue(goalTrackerToDelete.getDeleted());
       assertNotNull(goalTrackerToDelete.getUpdatedTime());
    }
    @Test
    void testDeleteGoalTracker_ResourceNotFoundException() {
       // Arrange
       Long goalTrackerId = 1L;

       when(goalTrackerRepository.findById(goalTrackerId)).thenReturn(Optional.empty());

       // Act and Assert
       assertThrows(ResourceNotFoundException.class, () -> goalTrackerService.deleteGoalTracker(goalTrackerId));
    }

    @Test
     void testGetAllGoalTrackersByEngagementId() {
        // Arrange
        Long engagementId = 1L;
        List<GoalTracker> goalTrackers = new ArrayList<>();
        goalTrackers.add(new GoalTracker());
        when(goalTrackerRepository.findAllByEngagementId(engagementId)).thenReturn(goalTrackers);

        // Mock the modelMapper behavior
        GoalTrackerDTO expectedDTO = new GoalTrackerDTO();
        when(modelMapper.map(any(GoalTracker.class), eq(GoalTrackerDTO.class))).thenReturn(expectedDTO);

        // Act
        List<GoalTrackerDTO> result = goalTrackerService.getAllGoalTrackersByEngagementId(engagementId);

        // Assert
        assertEquals(1, result.size());
        assertSame(expectedDTO, result.get(0));
    }
}







