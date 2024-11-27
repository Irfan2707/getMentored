package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.exception.DuplicateResourceException;
import com.nineleaps.authentication.jwt.repository.GoalTrackerRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IGoalTrackerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class GoalTrackerServiceImpl implements IGoalTrackerService {
    @Autowired
    private final GoalTrackerRepository goalTrackerRepository;
    @Autowired
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(GoalTrackerServiceImpl.class);

    @Override
    public GoalTrackerDTO createGoalTracker(GoalTrackerDTO goalTrackerDto) throws DuplicateResourceException {
        logger.info("Creating a goal tracker with description: '{}' for engagement ID: {}", goalTrackerDto.getDescription(), goalTrackerDto.getEngagementId());

        String description = goalTrackerDto.getDescription();
        Long engagementId = goalTrackerDto.getEngagementId();

        // Check if a goal tracker with the same description and engagement ID already exists
        boolean exists = goalTrackerRepository.existsByDescriptionAndEngagementId(description, engagementId);
        if (exists) {
            logger.error("A goal tracker with the same description and engagement ID already exists.");
            throw new DuplicateResourceException("A goal tracker with the same description and engagement ID already exists.");
        }

        GoalTracker goalTracker = modelMapper.map(goalTrackerDto, GoalTracker.class);
        goalTracker.setGoalTrackerStartTime(LocalDateTime.now()); // Set the current timestamp

        GoalTracker createdGoalTracker = goalTrackerRepository.save(goalTracker);
        return modelMapper.map(createdGoalTracker, GoalTrackerDTO.class);
    }


    @Override
    public GoalTrackerDTO updateGoalTracker(Long id, GoalTrackerDTO goalTrackerDto) {
        logger.info("Updating goal tracker with ID: {}", id);


        GoalTracker goalTracker = modelMapper.map(goalTrackerDto, GoalTracker.class);

        goalTracker.setId(id);

        goalTrackerRepository.save(goalTracker);

        logger.info("Goal tracker with ID {} updated successfully", id);


        return modelMapper.map(goalTracker, GoalTrackerDTO.class);
    }


    @Override
    public List<GoalTrackerDTO> getAllGoalTrackersByEngagementId(Long engagementId) {
        logger.info("Getting all goal trackers for engagement with ID: {}", engagementId);

        List<GoalTracker> goalTrackers = goalTrackerRepository.findAllByEngagementId(engagementId);

        logger.info("Retrieved {} goal trackers for engagement with ID: {}", goalTrackers.size(), engagementId);

        return goalTrackers.stream()
                .map(goalTracker -> modelMapper.map(goalTracker, GoalTrackerDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteGoalTracker(Long id) throws ResourceNotFoundException {
        logger.info("Deleting goal tracker with ID: {}", id);

        GoalTracker goalTracker = goalTrackerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GoalTracker", "id", id));

        goalTracker.setDeleted(true); // Soft delete by marking it as deleted
        goalTracker.setUpdatedTime(LocalDateTime.now());
        goalTrackerRepository.save(goalTracker); // Update the record with the deleted flag

        logger.info("Goal tracker with ID {} marked as deleted", id);
    }


}

