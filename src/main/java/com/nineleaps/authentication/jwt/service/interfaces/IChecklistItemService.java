package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;

import java.util.List;

public interface IChecklistItemService {
    ChecklistItemDTO createChecklistItem(Long userId, ChecklistItemDTO checklistItemDto);

    void deleteChecklistItem(Long id) throws ResourceNotFoundException;

    List<ChecklistItemDTO> getAllChecklistItemsByGoalTrackerId(int pageNumber, int pageSize, Long goalTrackerId);

    ChecklistItemDTO updateChecklistItem(Long id, ChecklistItemDTO checklistItemDto, Long userId)
            throws ResourceNotFoundException;

    ChecklistItemDTO updateChecklistItemStatus(Long userId, Long checklistItemId, ChecklistitemStatus newStatus) throws ResourceNotFoundException;

    List<ActivityLogDTO> getActivityDetailsByGoal(Long userId, Long goalId) throws ResourceNotFoundException;
}
