package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.ChecklistItemServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional
@RequestMapping("/api/v1/checklistItems")
public class ChecklistItemController {

    private final ChecklistItemServiceImpl checklistItemServiceImpl;
    private final Logger logger = LoggerFactory.getLogger(ChecklistItemController.class);


    /**
     * Endpoint for retrieving all checklist items associated with a specific goal tracker ID.
     *
     * @param goalTrackerId The ID of the goal tracker for which checklist items are to be retrieved.
     * @param pageNumber    The page number for paginated results.
     * @param pageSize      The number of items per page for paginated results.
     * @return A list of checklist items for the specified goal tracker ID.
     */
    @GetMapping("/{goalTrackerId}/checklistItems")
    @ApiOperation("Get All Checklist Items by Goal Tracker Id")
//    @PreAuthorize("hasAnyAuthority('MENTEE','MENTOR')")
    @ResponseStatus(HttpStatus.OK)
    public List<ChecklistItemDTO> getAllChecklistItemsByGoalTrackerId(
            @RequestParam("goalTrackerId") Long goalTrackerId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "100", required = false) int pageSize) {
        return checklistItemServiceImpl.getAllChecklistItemsByGoalTrackerId(pageNumber, pageSize, goalTrackerId);
    }

    /**
     * Endpoint for creating a checklist item for a goal.
     *
     * @param userId           The ID of the user creating the checklist item.
     * @param checklistItemDTO The data transfer object containing checklist item details.
     * @return A response entity indicating the result of the creation operation.
     */
    @PostMapping("/createChecklist")
    @ApiOperation("Create checklists for your Goal")
    public ResponseEntity<Object> createChecklistItem(@RequestParam Long userId, @RequestBody @Valid
    ChecklistItemDTO checklistItemDTO) {
        // Create a checklist item for a goal
        ChecklistItemDTO createdChecklistItem = checklistItemServiceImpl.createChecklistItem(userId, checklistItemDTO);

        if (createdChecklistItem == null) {
            logger.error("Failed to create checklist item.");
            return ResponseHandler.error("Failed to create checklist item.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Checklist item created successfully.");
        return ResponseHandler.success("Checklist item created successfully.", HttpStatus.CREATED, createdChecklistItem);
    }

    /**
     * Endpoint for updating the status of a checklist item for a goal.
     *
     * @param userId          The ID of the user updating the checklist item status.
     * @param checklistItemId The ID of the checklist item to be updated.
     * @param newStatus       The new status to be set for the checklist item.
     * @return A response entity indicating the result of the update operation.
     * @throws ResourceNotFoundException If the specified checklist item ID is not found.
     */
    @PutMapping("/updateStatus")
    @ApiOperation("Update Status of checklists for your Goal")
    public ResponseEntity<Object> updateChecklistItemStatus(
            @RequestParam("userId") Long userId,
            @RequestParam("id") Long checklistItemId,
            @RequestParam("status") ChecklistitemStatus newStatus) throws ResourceNotFoundException {
        // Update the status of a checklist item
        ChecklistItemDTO updatedChecklistItem = checklistItemServiceImpl.updateChecklistItemStatus(userId, checklistItemId, newStatus);

        if (updatedChecklistItem == null) {
            logger.error("Failed to update checklist item status.");
            return ResponseHandler.error("Failed to update checklist item status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Checklist item status updated successfully.");
        return ResponseHandler.success("Checklist item status updated successfully.", HttpStatus.OK, updatedChecklistItem);
    }

    /**
     * Endpoint for updating a checklist item by its ID.
     *
     * @param id               The ID of the checklist item to be updated.
     * @param checklistItemDto The data transfer object containing updated checklist item details.
     * @param userId           The ID of the user updating the checklist item.
     * @return A response entity indicating the result of the update operation.
     * @throws ResourceNotFoundException If the specified checklist item ID is not found.
     */
    @PutMapping("/updateById")
    @ApiOperation("Update checklist items by checklist Id")
    public ResponseEntity<Object> updateChecklistItem(
            @RequestParam("id") Long id,
            @RequestBody @Valid ChecklistItemDTO checklistItemDto,
            @RequestParam("userId") Long userId) throws ResourceNotFoundException {
        // Update a checklist item by its ID
        ChecklistItemDTO updatedChecklistItem = checklistItemServiceImpl.updateChecklistItem(id, checklistItemDto, userId);

        if (updatedChecklistItem == null) {
            logger.error("Failed to update checklist item.");
            return ResponseHandler.error("Failed to update checklist item.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Checklist item updated successfully.");
        return ResponseHandler.success("Checklist item updated successfully.", HttpStatus.OK, updatedChecklistItem);
    }

    /**
     * Endpoint for retrieving activity history for a specific user and goal.
     *
     * @param userId The ID of the user for whom the activity history is to be retrieved.
     * @param goalId The ID of the goal for which the activity history is to be retrieved.
     * @return A response entity containing the activity history for the specified user and goal.
     * @throws ResourceNotFoundException If no activity history is found for the specified user and goal.
     */
    @GetMapping("/getActivityLogByUserIdAndGoalId")
    @ApiOperation("Get Activity history for the Goal")
    public ResponseEntity<Object> getActivityDetailsByGoal(@RequestParam("user_id") Long userId,
                                                           @RequestParam("goal_id") Long goalId) throws ResourceNotFoundException {
        // Retrieve activity history for a goal
        List<ActivityLogDTO> activityLogs = checklistItemServiceImpl.getActivityDetailsByGoal(userId, goalId);

        if (activityLogs.isEmpty()) {
            logger.info("No activity history found for the given user and goal. userId: {}, goalId: {}", userId, goalId);
            return ResponseHandler.error("No activity history found for the given user and goal.", HttpStatus.NOT_FOUND);
        }

        logger.info("Activity history retrieved successfully for userId: {} and goalId: {}", userId, goalId);
        return ResponseHandler.success("Activity history retrieved successfully.", HttpStatus.OK, activityLogs);
    }

    /**
     * Endpoint for deleting a checklist item by its ID.
     *
     * @param id The ID of the checklist item to be deleted.
     * @return A response entity indicating the result of the deletion operation.
     */
    @DeleteMapping("/deleteById")
    @ApiOperation("Delete the checklist item by the ID")
    public ResponseEntity<Object> deleteChecklistItem(@RequestParam("id") Long id) {
        checklistItemServiceImpl.deleteChecklistItem(id);

        logger.info("Checklist item deleted successfully. ID: {}", id);
        return ResponseHandler.success("Checklist item deleted successfully.", HttpStatus.OK, null);
    }


}
