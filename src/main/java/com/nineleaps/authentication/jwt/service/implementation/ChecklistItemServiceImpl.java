package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.entity.ActivityLog;
import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import com.nineleaps.authentication.jwt.repository.ActivityLogRepository;
import com.nineleaps.authentication.jwt.repository.ChecklistItemRepository;
import com.nineleaps.authentication.jwt.repository.GoalTrackerRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IChecklistItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor

public class ChecklistItemServiceImpl implements IChecklistItemService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ChecklistItemRepository checklistItemRepository;
    @Autowired
    private final GoalTrackerRepository goalTrackerRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final ActivityLogRepository activityLogRepository;

    private static final Logger logger = LoggerFactory.getLogger(ChecklistItemServiceImpl.class);


    @Override
    public List<ChecklistItemDTO> getAllChecklistItemsByGoalTrackerId(int pageNumber, int pageSize, Long goalTrackerId) {
        try {
            logger.info("Fetching all checklist items by Goal Tracker ID: {}", goalTrackerId);
            long startTime = System.currentTimeMillis();

            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            Page<ChecklistItem> checklistItemsPage = checklistItemRepository.findAllByGoalTrackerId(goalTrackerId, pageable);

            long endTime = System.currentTimeMillis();
            logger.info("Query execution time: {} milliseconds", (endTime - startTime));
            if (checklistItemsPage.isEmpty()) {
                logger.info("No checklist items found for Goal Tracker ID: {}", goalTrackerId);
                return Collections.emptyList();
            }

            List<ChecklistItemDTO> checklistItemDTOs = new ArrayList<>();
            for (ChecklistItem checklistItem : checklistItemsPage) {
                ChecklistItemDTO checklistItemDTO = getChecklistItemDTO(checklistItem);
                checklistItemDTOs.add(checklistItemDTO);
            }


            logger.info("Fetched {} checklist items for Goal Tracker ID: {}", checklistItemsPage.getTotalElements(), goalTrackerId);

            return checklistItemDTOs;
        } catch (Exception ex) {
            logger.error("An error occurred while fetching checklist items: {}", ex.getMessage(), ex);
            return Collections.emptyList(); // Return an empty list or handle the error as needed
        }
    }

    public static ChecklistItemDTO getChecklistItemDTO(ChecklistItem checklistItem) {
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();
        checklistItemDTO.setId(checklistItem.getId());
        checklistItemDTO.setGoalTrackerId(checklistItem.getGoalTracker().getId());
        checklistItemDTO.setItemDescription(checklistItem.getItemDescription());
        checklistItemDTO.setStatus(checklistItem.getStatus());
        checklistItemDTO.setCreatedAt(checklistItem.getCreatedAt());
        checklistItemDTO.setUpdatedAt(checklistItem.getUpdatedAt());
        return checklistItemDTO;
    }

    @Override
    public ChecklistItemDTO updateChecklistItem(Long id, ChecklistItemDTO checklistItemDto, Long userId) throws ResourceNotFoundException {
        logger.info("Updating checklist item with ID: {}", id);

        ChecklistItem existingChecklistItem = checklistItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with id " + id));

        LocalDateTime createdAt = existingChecklistItem.getCreatedAt(); // Get the existing createdAt timestamp

        modelMapper.map(checklistItemDto, existingChecklistItem);

        existingChecklistItem.setCreatedAt(createdAt); // Set the original createdAt timestamp
        existingChecklistItem.setUpdatedAt(LocalDateTime.now()); // Set the updatedAt timestamp

        ChecklistItem updatedChecklistItem = checklistItemRepository.save(existingChecklistItem);

        // Fetch the user and set it in the activity log
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.orElseThrow(() -> new ResourceNotFoundException("User not found with the ID: " + userId));

        // Create an activity log for the update
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityType("Updated Task");
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setUser(user);
        activityLog.setChecklistItem(updatedChecklistItem);

        // Set the username in the activity log
        String username = user.getUserName();
        activityLog.setUserName(username);

        updatedChecklistItem.addActivityLog(activityLog);

        // Save the updated checklist item with the activity log
        updatedChecklistItem = checklistItemRepository.save(updatedChecklistItem);

        logger.info("Checklist item with ID {} updated successfully", id);

        return getChecklistItemDTO(updatedChecklistItem);
    }


    @Override
    @Transactional
    public void deleteChecklistItem(Long id) {
        logger.info("Deleting checklist item with ID: {}", id);

        ChecklistItem checklistItem = checklistItemRepository.findById(id)
                .orElseThrow();

        // Create a new activity log entry
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setActivityType("Checklist Item Deleted");
        activityLog.setChecklistItem(checklistItem); // Associate the checklist item with the activity log
        if (checklistItem.getActivityLogs() == null) {
            checklistItem.setActivityLogs(new ArrayList<>());
        }

        checklistItem.getActivityLogs().add(activityLog);

        activityLogRepository.save(activityLog); // Save the activity log

        // Set soft delete flags
        checklistItem.setDeleted(true);
        checklistItem.setUpdatedAt(LocalDateTime.now());

        // Save the updated checklist item with soft delete
        checklistItemRepository.save(checklistItem);

        logger.info("Checklist item with ID {} soft deleted successfully", id);
    }


    @Override
    public ChecklistItemDTO createChecklistItem(Long userId, ChecklistItemDTO checklistItemDTO) throws ResourceNotFoundException {
        logger.info("Creating checklist item for user with ID: {}", userId);

        // Fetch the user or throw ResourceNotFoundException if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with the proper ID: " + userId));

        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setItemDescription(checklistItemDTO.getItemDescription());
        checklistItem.setStatus(ChecklistitemStatus.PENDING);
        checklistItem.setCreatedAt(LocalDateTime.now());

        // Fetch the associated GoalTracker or throw ResourceNotFoundException if not found
        GoalTracker goalTracker = goalTrackerRepository.findById(checklistItemDTO.getGoalTrackerId())
                .orElseThrow(() -> new ResourceNotFoundException("GoalTracker not found with the proper ID: " + checklistItemDTO.getGoalTrackerId()));
        checklistItem.setGoalTracker(goalTracker);

        ChecklistItem savedChecklistItem = checklistItemRepository.save(checklistItem);

        // Create an activity log for the creation
        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityType("Created Task");
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setUser(user);
        activityLog.setUserName(user.getUserName());
        activityLog.setChecklistItem(savedChecklistItem);

        // Save the activity log
        activityLogRepository.save(activityLog);

        // Convert the saved checklist item to DTO
        ChecklistItemDTO savedChecklistItemDTO = convertToDTO(savedChecklistItem);
        savedChecklistItemDTO.setCreatedAt(savedChecklistItem.getCreatedAt()); // Set the createdAt timestamp

        logger.info("Checklist item created successfully with ID: {}", savedChecklistItemDTO.getId());

        return savedChecklistItemDTO;
    }


    @Override
    public ChecklistItemDTO updateChecklistItemStatus(Long userId, Long checklistItemId, ChecklistitemStatus newStatus) throws ResourceNotFoundException {
        logger.info("Updating checklist item status for checklist item ID: {} to {}", checklistItemId, newStatus);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        ChecklistItem checklistItem = checklistItemRepository.findById(checklistItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Checklist item not found with ID: " + checklistItemId));

        // Make sure the goalTracker property is set correctly
        GoalTracker goalTracker = checklistItem.getGoalTracker();
        if (goalTracker == null) {
            throw new ResourceNotFoundException("GoalTracker not found for the ChecklistItem with ID: " + checklistItemId);
        }

        ActivityLog activityLog = new ActivityLog();
        activityLog.setActivityTime(LocalDateTime.now());
        activityLog.setUser(user);
        activityLog.setChecklistItem(checklistItem);
        String username = user.getUserName();
        activityLog.setUserName(username);

        if (newStatus == ChecklistitemStatus.DONE) {
            activityLog.setActivityType("Completed");
        } else {
            activityLog.setActivityType("Marked as incomplete");
        }

        activityLogRepository.save(activityLog);

        checklistItem.setStatus(newStatus);
        checklistItem.setUpdatedAt(LocalDateTime.now());
        ChecklistItem updatedChecklistItem = checklistItemRepository.save(checklistItem);

        ChecklistItemDTO updatedChecklistItemDTO = convertToDTO(updatedChecklistItem);
        updatedChecklistItemDTO.setCreatedAt(checklistItem.getCreatedAt());
        updatedChecklistItemDTO.setUpdatedAt(checklistItem.getUpdatedAt());
        logger.info("Checklist item status updated successfully for ID: {}", checklistItemId);

        return updatedChecklistItemDTO;
    }


    public ChecklistItem convertToEntity(ChecklistItemDTO checklistItemDTO) {
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItem.setId(checklistItemDTO.getId());
        checklistItem.setGoalTracker(goalTrackerRepository.findById(checklistItemDTO.getGoalTrackerId())
                .orElseThrow());
        checklistItem.setItemDescription(checklistItemDTO.getItemDescription());
        checklistItem.setStatus(checklistItemDTO.getStatus());
        return checklistItem;
    }

    public ChecklistItemDTO convertToDTO(ChecklistItem checklistItem) {
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();
        checklistItemDTO.setId(checklistItem.getId());
        checklistItemDTO.setGoalTrackerId(checklistItem.getGoalTracker().getId());
        checklistItemDTO.setItemDescription(checklistItem.getItemDescription());
        checklistItemDTO.setStatus(checklistItem.getStatus());
        return checklistItemDTO;
    }

    @Override
    public List<ActivityLogDTO> getActivityDetailsByGoal(Long userId, Long goalId) throws ResourceNotFoundException {
        logger.info("Getting activity details for user with ID: {} and goal with ID: {}", userId, goalId);


        List<ChecklistItem> checklists = checklistItemRepository.findAllByGoalTrackerId(goalId);

        List<ActivityLogDTO> activityLogs = new ArrayList<>();

        for (ChecklistItem checklist : checklists) {
            List<ActivityLog> checklistActivityLogs = activityLogRepository.findByChecklistItem(checklist);

            for (ActivityLog activityLog : checklistActivityLogs) {
                ActivityLogDTO activityLogDTO = modelMapper.map(activityLog, ActivityLogDTO.class);
                if (activityLogDTO != null) {
                    activityLogDTO.setChecklistId(checklist.getId());
                    activityLogDTO.setItemDescription(checklist.getItemDescription());
                    activityLogs.add(activityLogDTO);
                }
            }
        }

        if (activityLogs.isEmpty()) {
            // Return an empty list with 200 response
            return Collections.emptyList();
        }
        activityLogs.sort(Comparator.comparing(ActivityLogDTO::getActivityTime).reversed());

        logger.info("Activity details retrieved successfully for user with ID: {} and goal with ID: {}", userId, goalId);

        return activityLogs;
    }


}




