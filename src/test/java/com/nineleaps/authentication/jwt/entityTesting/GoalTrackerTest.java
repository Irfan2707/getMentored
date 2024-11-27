package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import com.nineleaps.authentication.jwt.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoalTrackerTest {

    @Test
    void testGoalTrackerAttributesAndRelationships() {
        Long id = 1L;
        Engagement engagement = new Engagement();
        String description = "Track goals";
        List<ChecklistItem> checklistItems = new ArrayList<>();
        User user = new User();
        LocalDateTime goalTrackerStartTime = LocalDateTime.now();
        LocalDateTime goalTrackerUpdatedTime=LocalDateTime.now().plusHours(1);
        boolean is_deleted=false;

        GoalTracker goalTracker = new GoalTracker(id, engagement, description, checklistItems, user, goalTrackerStartTime,goalTrackerUpdatedTime,is_deleted);

        assertEquals(id, goalTracker.getId());
        assertEquals(engagement, goalTracker.getEngagement());
        assertEquals(description, goalTracker.getDescription());
        assertEquals(checklistItems, goalTracker.getChecklistItems());
        assertEquals(user, goalTracker.getUser());
        assertEquals(goalTrackerStartTime, goalTracker.getGoalTrackerStartTime());
    }

    @Test
    void testAddChecklistItem() {
        GoalTracker goalTracker = new GoalTracker();
        List<ChecklistItem> checklistItems=new ArrayList<>();
        ChecklistItem checklistItem = new ChecklistItem();
        checklistItems.add(checklistItem);

        goalTracker.setChecklistItems(checklistItems);

        List<ChecklistItem> checklistItem1 = goalTracker.getChecklistItems();
        assertNotNull(checklistItems);
        assertEquals(1, checklistItems.size());
        assertEquals(checklistItem, checklistItems.get(0));

    }
    // Test the NoArgsConstructor

    @Test
    void testNoArgsConstructor() {
        GoalTracker goalTracker = new GoalTracker();

        assertNull(goalTracker.getId());
        assertNull(goalTracker.getEngagement());
        assertNull(goalTracker.getDescription());
        assertNotNull(goalTracker.getChecklistItems());
        assertTrue(goalTracker.getChecklistItems().isEmpty());
        assertNull(goalTracker.getUser());
        assertNull(goalTracker.getGoalTrackerStartTime());
    }
}
