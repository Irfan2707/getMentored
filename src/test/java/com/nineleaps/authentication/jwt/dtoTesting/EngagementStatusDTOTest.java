package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.EngagementStatusDTO;
import com.nineleaps.authentication.jwt.enums.EngStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class EngagementStatusDTOTest {

    @Test
    void testNoArgsConstructor() {
        EngagementStatusDTO statusDTO = new EngagementStatusDTO();

        assertNull(statusDTO.getEngagementStatusId());
        assertNull(statusDTO.getEngagementId());
        assertNull(statusDTO.getMentorEngStatus());
        assertNull(statusDTO.getMenteeEngStatus());
        assertNull(statusDTO.getMentorStatusTimestamp());
        assertNull(statusDTO.getMenteeStatusTimestamp());
        assertNull(statusDTO.getCompletedEngStatusTimestamp());
    }

    @Test
    void testAllArgsConstructor() {
        Long engagementStatusId = 1L;
        Long engagementId = 101L;

        EngStatus mentorEngStatus = EngStatus.PENDING;
        EngStatus menteeEngStatus = EngStatus.DONE;
        LocalDateTime mentorStatusTimestamp = LocalDateTime.now();
        LocalDateTime menteeStatusTimestamp = LocalDateTime.now();
        LocalDateTime completedEngStatusTimestamp = LocalDateTime.now();

        EngagementStatusDTO statusDTO = new EngagementStatusDTO(
                engagementStatusId,
                engagementId,
                mentorEngStatus,
                menteeEngStatus,
                mentorStatusTimestamp,
                menteeStatusTimestamp,
                completedEngStatusTimestamp
        );

        assertEquals(engagementStatusId, statusDTO.getEngagementStatusId());
        assertEquals(engagementId, statusDTO.getEngagementId());
        assertEquals(mentorEngStatus, statusDTO.getMentorEngStatus());
        assertEquals(menteeEngStatus, statusDTO.getMenteeEngStatus());
        assertEquals(mentorStatusTimestamp, statusDTO.getMentorStatusTimestamp());
        assertEquals(menteeStatusTimestamp, statusDTO.getMenteeStatusTimestamp());
        assertEquals(completedEngStatusTimestamp, statusDTO.getCompletedEngStatusTimestamp());
    }

    @Test
    void testGettersAndSetters() {
        EngagementStatusDTO statusDTO = new EngagementStatusDTO();

        Long engagementStatusId = 1L;
        Long engagementId = 101L;
        EngStatus mentorEngStatus = EngStatus.PENDING;
        EngStatus menteeEngStatus = EngStatus.DONE;
        LocalDateTime mentorStatusTimestamp = LocalDateTime.now();
        LocalDateTime menteeStatusTimestamp = LocalDateTime.now();
        LocalDateTime completedEngStatusTimestamp = LocalDateTime.now();

        statusDTO.setEngagementStatusId(engagementStatusId);
        statusDTO.setEngagementId(engagementId);
        statusDTO.setMentorEngStatus(mentorEngStatus);
        statusDTO.setMenteeEngStatus(menteeEngStatus);
        statusDTO.setMentorStatusTimestamp(mentorStatusTimestamp);
        statusDTO.setMenteeStatusTimestamp(menteeStatusTimestamp);
        statusDTO.setCompletedEngStatusTimestamp(completedEngStatusTimestamp);

        assertEquals(engagementStatusId, statusDTO.getEngagementStatusId());
        assertEquals(engagementId, statusDTO.getEngagementId());
        assertEquals(mentorEngStatus, statusDTO.getMentorEngStatus());
        assertEquals(menteeEngStatus, statusDTO.getMenteeEngStatus());
        assertEquals(mentorStatusTimestamp, statusDTO.getMentorStatusTimestamp());
        assertEquals(menteeStatusTimestamp, statusDTO.getMenteeStatusTimestamp());
        assertEquals(completedEngStatusTimestamp, statusDTO.getCompletedEngStatusTimestamp());
    }
}
