package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.GoalTrackerDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class GoalTrackerDTOTest {

    @Test
    void testNoArgsConstructor() {
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO();

        assertNull(goalTrackerDTO.getId());
        assertNull(goalTrackerDTO.getEngagementId());
        assertNull(goalTrackerDTO.getDescription());
        assertNull(goalTrackerDTO.getUserId());
        assertNull(goalTrackerDTO.getGoalTrackerStartTime());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = 1L;
        Long engagementId = 101L;
        String description = "Complete Goal tasks";
        Long userId = 201L;
        LocalDateTime goalTrackerStartTime = LocalDateTime.now();

        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO(
                id,
                engagementId,
                description,
                userId,
                goalTrackerStartTime
        );

        assertEquals(id, goalTrackerDTO.getId());
        assertEquals(engagementId, goalTrackerDTO.getEngagementId());
        assertEquals(description, goalTrackerDTO.getDescription());
        assertEquals(userId, goalTrackerDTO.getUserId());
        assertEquals(goalTrackerStartTime, goalTrackerDTO.getGoalTrackerStartTime());
    }

    @Test
    void testGettersAndSetters() {
        GoalTrackerDTO goalTrackerDTO = new GoalTrackerDTO();

        Long id = 1L;
        Long engagementId = 101L;
        String description = "Complete Goal tasks";
        Long userId = 201L;
        LocalDateTime goalTrackerStartTime = LocalDateTime.now();

        goalTrackerDTO.setId(id);
        goalTrackerDTO.setEngagementId(engagementId);
        goalTrackerDTO.setDescription(description);
        goalTrackerDTO.setUserId(userId);
        goalTrackerDTO.setGoalTrackerStartTime(goalTrackerStartTime);

        assertEquals(id, goalTrackerDTO.getId());
        assertEquals(engagementId, goalTrackerDTO.getEngagementId());
        assertEquals(description, goalTrackerDTO.getDescription());
        assertEquals(userId, goalTrackerDTO.getUserId());
        assertEquals(goalTrackerStartTime, goalTrackerDTO.getGoalTrackerStartTime());
    }
}
