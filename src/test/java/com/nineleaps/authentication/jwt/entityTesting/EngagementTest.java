package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.EngagementStatus;
import com.nineleaps.authentication.jwt.entity.GoalTracker;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EngagementTest {

    @Test
    void testEngagementAttributesAndRelationships() {
        Long id = 1L;
        ConnectionRequest connectionRequest = new ConnectionRequest();
        LocalDateTime startTime = LocalDateTime.now();
        int durationHours = 2;
        GoalTracker goalTracker = new GoalTracker();
        List<EngagementStatus> engagementStatuses = new ArrayList<>();

        Engagement engagement = new Engagement(id, connectionRequest, startTime, durationHours, goalTracker, engagementStatuses);

        assertEquals(id, engagement.getId());
        assertEquals(connectionRequest, engagement.getConnectionRequest());
        assertEquals(startTime, engagement.getStartTime());
        assertEquals(durationHours, engagement.getDurationHours());
        assertEquals(goalTracker, engagement.getGoalTracker());
        assertEquals(engagementStatuses, engagement.getEngagementStatuses());
    }
    // Test the NoArgsConstructor

    @Test
    void testNoArgsConstructor() {
        Engagement engagement = new Engagement();

        assertNull(engagement.getId());
        assertNull(engagement.getConnectionRequest());
        assertNull(engagement.getStartTime());
        assertEquals(0, engagement.getDurationHours());
        assertNull(engagement.getGoalTracker());
        assertNotNull(engagement.getEngagementStatuses());
        assertTrue(engagement.getEngagementStatuses().isEmpty());
    }


}
