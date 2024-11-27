package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.ActivityLog;
import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChecklistItemTest {

    private Validator validator;
    @Mock
    private ChecklistItem checklistItem;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        checklistItem = new ChecklistItem();
    }

    @Test
    void testAddActivityLog() {
        ChecklistItem checklistItem = new ChecklistItem();
        Long id = 1L;
        GoalTracker goalTracker = new GoalTracker();
        ChecklistitemStatus status = ChecklistitemStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        String itemDescription = "Test Checklist Item";

        checklistItem.setId(id);
        checklistItem.setGoalTracker(goalTracker);
        checklistItem.setStatus(status);
        checklistItem.setCreatedAt(createdAt);
        checklistItem.setUpdatedAt(updatedAt);
        checklistItem.setItemDescription(itemDescription);

        ActivityLog activityLog = new ActivityLog();
        Long activityId = 1L;
        String activityType = "Test Activity";
        LocalDateTime activityTime = LocalDateTime.now();
        String userName = "testUser";

        activityLog.setId(activityId);
        activityLog.setActivityType(activityType);
        activityLog.setActivityTime(activityTime);
        activityLog.setUserName(userName);

        checklistItem.addActivityLog(activityLog);

        List<ActivityLog> activityLogs = checklistItem.getActivityLogs();

        assertNotNull(activityLogs);
        assertEquals(1, activityLogs.size());
        assertEquals(activityLog, activityLogs.get(0));
        assertEquals(checklistItem, activityLog.getChecklistItem());
    }

    @Test
    void testNoArgsConstructor() {
        ChecklistItem checklistItem = new ChecklistItem();

        assertNull(checklistItem.getId());
        assertNull(checklistItem.getGoalTracker());
        assertNull(checklistItem.getStatus());
        assertNull(checklistItem.getCreatedAt());
        assertNull(checklistItem.getUpdatedAt());
        assertNull(checklistItem.getItemDescription());
        assertNull(checklistItem.getActivityLogs());
    }


    @Test
    void testAddActivityLogWhenActivityLogsIsNull() {
        assertNull(checklistItem.getActivityLogs());

        ActivityLog activityLog = new ActivityLog();
        checklistItem.addActivityLog(activityLog);

        assertNotNull(checklistItem.getActivityLogs());
        assertEquals(1, checklistItem.getActivityLogs().size());
        assertTrue(checklistItem.getActivityLogs().contains(activityLog));
        assertEquals(checklistItem, activityLog.getChecklistItem());
    }

    @Test
    void testAddActivityLogWhenActivityLogsIsNotNull() {
        List<ActivityLog> existingLogs = new ArrayList<>();
        ActivityLog existingLog = new ActivityLog();
        existingLogs.add(existingLog);
        checklistItem.setActivityLogs(existingLogs);

        ActivityLog newLog = new ActivityLog();
        checklistItem.addActivityLog(newLog);

        assertNotNull(checklistItem.getActivityLogs());
        assertEquals(2, checklistItem.getActivityLogs().size());
        assertTrue(checklistItem.getActivityLogs().contains(newLog));
        assertEquals(checklistItem, newLog.getChecklistItem());
    }



}