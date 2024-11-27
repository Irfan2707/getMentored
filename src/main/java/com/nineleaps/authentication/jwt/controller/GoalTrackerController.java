package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.interfaces.IGoalTrackerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "goal-trackers")
@AllArgsConstructor
@RequestMapping("/api/v1/goalTrackers")
public class GoalTrackerController {

    private final IGoalTrackerService iGoalTrackerService;
    private final Logger logger = LoggerFactory.getLogger(GoalTrackerController.class);


    /**
     * Endpoint for creating a Goal Tracker for an Engagement.
     *
     * @param goalTrackerDTO The Goal Tracker details to be created.
     * @return A response entity indicating the result of the Goal Tracker creation process.
     */
    @PostMapping("/createGoal")
    @ApiOperation("Create a Goal Tracker for your Engagement")
    public ResponseEntity<Object> createGoalTracker(@RequestBody GoalTrackerDTO goalTrackerDTO) {
        GoalTrackerDTO createdGoalTracker = iGoalTrackerService.createGoalTracker(goalTrackerDTO);

        if (createdGoalTracker != null) {
            logger.info("Goal Tracker created successfully.");
            return ResponseHandler.success("Goal Tracker created successfully.", HttpStatus.CREATED, createdGoalTracker);
        } else {
            logger.error("Failed to create Goal Tracker.");
            return ResponseHandler.error("Failed to create Goal Tracker.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint for updating Goal Tracker details by Goal Tracker ID.
     *
     * @param id             The ID of the Goal Tracker to be updated.
     * @param goalTrackerDTO The updated Goal Tracker details.
     * @return A response entity indicating the result of the Goal Tracker update process.
     * @throws ResourceNotFoundException if the Goal Tracker with the given ID is not found.
     */
    @PutMapping("/updateById")
    @ApiOperation("Update Goal Tracker Details By Goal Tracker Id")
    public ResponseEntity<Object> updateGoalTracker(@RequestParam Long id, @RequestBody GoalTrackerDTO goalTrackerDTO) throws ResourceNotFoundException {
        GoalTrackerDTO updatedGoalTracker = iGoalTrackerService.updateGoalTracker(id, goalTrackerDTO);

        if (updatedGoalTracker != null) {
            logger.info("Goal Tracker updated successfully.");
            return ResponseHandler.success("Goal Tracker updated successfully.", HttpStatus.OK, updatedGoalTracker);
        } else {
            logger.error("Failed to update Goal Tracker with ID: {}", id);
            return ResponseHandler.error("Failed to update Goal Tracker.", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Endpoint for deleting a Goal Tracker by Goal Tracker ID.
     *
     * @param id The ID of the Goal Tracker to be deleted.
     * @return A response entity indicating the result of the Goal Tracker deletion process.
     */
    @DeleteMapping("/deleteById")
    @ApiOperation("Delete a Goal Tracker By Goal Tracker Id")
    public ResponseEntity<Object> deleteGoalTracker(@RequestParam Long id) {
        try {
            iGoalTrackerService.deleteGoalTracker(id);
            logger.info("Goal Tracker with ID {} deleted successfully.", id);
            return ResponseHandler.success("Goal Tracker deleted successfully.", HttpStatus.OK, null);
        } catch (ResourceNotFoundException e) {
            logger.error("Failed to delete Goal Tracker with ID: {}", id);
            return ResponseHandler.error("Failed to delete Goal Tracker.", HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Endpoint for retrieving all Goal Trackers by Engagement ID.
     *
     * @param engagementId The ID of the Engagement for which to retrieve Goal Trackers.
     * @return A response entity containing the list of Goal Trackers for the specified Engagement.
     */
    @GetMapping("/getAllGoalTrackersByEngagementId")
    @ApiOperation("Get All the Goal Tracker Details By Engagement Id")
    public ResponseEntity<Object> getAllGoalTrackersByEngagementId(@RequestParam Long engagementId) {
        List<GoalTrackerDTO> goalTrackers = iGoalTrackerService.getAllGoalTrackersByEngagementId(engagementId);

        if (goalTrackers == null) {
            logger.error("Failed to retrieve Goal Trackers for Engagement ID: {}", engagementId);
            return ResponseHandler.error("Failed to retrieve Goal Trackers.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!goalTrackers.isEmpty()) {
            logger.info("Retrieved Goal Trackers by Engagement ID: {}", engagementId);
            return ResponseHandler.success("Goal Trackers retrieved successfully.", HttpStatus.OK, goalTrackers);
        } else {
            logger.warn("No Goal Trackers found for Engagement ID: {}", engagementId);
            return ResponseHandler.error("No Goal Trackers found for the given Engagement ID.", HttpStatus.NOT_FOUND);
        }
    }


}

