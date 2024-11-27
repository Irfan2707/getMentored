package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.ActivityLog;
import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import com.nineleaps.authentication.jwt.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class ActivityLogTest {

    @Test
    void testEntityAttributesAndRelationships() {
        Long id = 1L;
        String activityType = "Login";
        LocalDateTime activityTime = LocalDateTime.now();
        String userName = "testUser";

        ActivityLog activityLog = new ActivityLog();
        activityLog.setId(id);
        activityLog.setActivityType(activityType);
        activityLog.setActivityTime(activityTime);
        activityLog.setUserName(userName);

        assertEquals(id, activityLog.getId());
        assertEquals(activityType, activityLog.getActivityType());
        assertEquals(activityTime, activityLog.getActivityTime());
        assertEquals(userName, activityLog.getUserName());

        ChecklistItem checklistItem = new ChecklistItem();
        User user = new User();

        activityLog.setChecklistItem(checklistItem);
        activityLog.setUser(user);

        assertEquals(checklistItem, activityLog.getChecklistItem());
        assertEquals(user, activityLog.getUser());
    }

    @Test
    void testActivityLogEntity() {
        Long id = 1L;
        String activityType = "Login";
        LocalDateTime activityTime = LocalDateTime.now();
        String userName = "testUser";

        ActivityLog activityLog = new ActivityLog();
        activityLog.setId(id);
        activityLog.setActivityType(activityType);
        activityLog.setActivityTime(activityTime);
        activityLog.setUserName(userName);

        assertEquals(id, activityLog.getId());
        assertEquals(activityType, activityLog.getActivityType());
        assertEquals(activityTime, activityLog.getActivityTime());
        assertEquals(userName, activityLog.getUserName());

        ChecklistItem checklistItem = new ChecklistItem();
        User user = new User();

        activityLog.setChecklistItem(checklistItem);
        activityLog.setUser(user);

        assertEquals(checklistItem, activityLog.getChecklistItem());
        assertEquals(user, activityLog.getUser());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        String activityType = "Login";
        LocalDateTime activityTime = LocalDateTime.now();
        String userName = "testUser";
        ChecklistItem checklistItem = new ChecklistItem();
        User user = new User();

        ActivityLog activityLog = new ActivityLog(id, activityType, activityTime, userName, checklistItem, user);

        assertEquals(id, activityLog.getId());
        assertEquals(activityType, activityLog.getActivityType());
        assertEquals(activityTime, activityLog.getActivityTime());
        assertEquals(userName, activityLog.getUserName());
        assertEquals(checklistItem, activityLog.getChecklistItem());
        assertEquals(user, activityLog.getUser());
    }

    @Test
    void testNoArgsConstructor() {

        ActivityLog activityLog = new ActivityLog();

        assertNull(activityLog.getId());
        assertNull(activityLog.getActivityType());
        assertNull(activityLog.getActivityTime());
        assertNull(activityLog.getUserName());
        assertNull(activityLog.getChecklistItem());
        assertNull(activityLog.getUser());
    }
}
