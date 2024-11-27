package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import com.nineleaps.authentication.jwt.exception.DuplicateResourceException;

import java.util.List;

public interface IGoalTrackerService {


    GoalTrackerDTO createGoalTracker(GoalTrackerDTO goalTrackerDto) throws DuplicateResourceException;


    List<GoalTrackerDTO> getAllGoalTrackersByEngagementId(Long engagementId);

    GoalTrackerDTO updateGoalTracker(Long id, GoalTrackerDTO goalTrackerDto) throws ResourceNotFoundException;

    void deleteGoalTracker(Long id) throws ResourceNotFoundException;
}
