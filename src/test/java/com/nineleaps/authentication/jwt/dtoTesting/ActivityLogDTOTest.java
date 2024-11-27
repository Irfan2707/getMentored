package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ActivityLogDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

 class ActivityLogDTOTest {

    @Test
    void testActivityLogDTOConstructorAndGetters() {
        LocalDateTime activityTime = LocalDateTime.now();

        ActivityLogDTO activityLogDTO = new ActivityLogDTO(
                1L,
                "Log1",
                activityTime,
                "User123",
                1L,
                "checklist completed"
        );

        assertEquals(1L, activityLogDTO.getId());
        assertEquals("Log1", activityLogDTO.getActivityType());
        assertEquals(activityTime, activityLogDTO.getActivityTime());
        assertEquals("User123", activityLogDTO.getUserName());
        assertEquals(1L, activityLogDTO.getChecklistId());
        assertEquals("checklist completed", activityLogDTO.getItemDescription());
    }

    @Test
    void testActivityLogDTONoArgsConstructor() {
        ActivityLogDTO activityLogDTO = new ActivityLogDTO();

        assertNotNull(activityLogDTO);
        assertNull(activityLogDTO.getId());
        assertNull(activityLogDTO.getActivityType());
        assertNull(activityLogDTO.getActivityTime());
        assertNull(activityLogDTO.getUserName());
        assertNull(activityLogDTO.getChecklistId());
        assertNull(activityLogDTO.getItemDescription());
    }

    @Test
    void testActivityLogDTOSetters() {
        ActivityLogDTO activityLogDTO = new ActivityLogDTO();

        LocalDateTime activityTime = LocalDateTime.now();

        activityLogDTO.setId(2L);
        activityLogDTO.setActivityType("Logout");
        activityLogDTO.setActivityTime(activityTime);
        activityLogDTO.setUserName("User456");
        activityLogDTO.setChecklistId(202L);
        activityLogDTO.setItemDescription("checklist completed");

        assertEquals(2L, activityLogDTO.getId());
        assertEquals("Logout", activityLogDTO.getActivityType());
        assertEquals(activityTime, activityLogDTO.getActivityTime());
        assertEquals("User456", activityLogDTO.getUserName());
        assertEquals(202L, activityLogDTO.getChecklistId());
        assertEquals("checklist completed", activityLogDTO.getItemDescription());
    }
}
