package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.EngagementStatus;
import com.nineleaps.authentication.jwt.enums.EngStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EngagementStatusTest {

    @Test
    void testEngagementStatusAttributesAndRelationships() {
        Long engagementStatusId = 1L;
        Engagement engagement = new Engagement();
        EngStatus mentorEngStatus = EngStatus.PENDING;
        EngStatus menteeEngStatus = EngStatus.PENDING;
        LocalDateTime mentorStatusTimestamp = LocalDateTime.now();
        LocalDateTime menteeStatusTimestamp = LocalDateTime.now();
        LocalDateTime completedEngStatusTimestamp = LocalDateTime.now();

        EngagementStatus engagementStatus = new EngagementStatus(engagementStatusId, engagement, mentorEngStatus, menteeEngStatus, mentorStatusTimestamp, menteeStatusTimestamp, completedEngStatusTimestamp);

        assertEquals(engagementStatusId, engagementStatus.getEngagementStatusId());
        assertEquals(engagement, engagementStatus.getEngagement());
        assertEquals(mentorEngStatus, engagementStatus.getMentorEngStatus());
        assertEquals(menteeEngStatus, engagementStatus.getMenteeEngStatus());
        assertEquals(mentorStatusTimestamp, engagementStatus.getMentorStatusTimestamp());
        assertEquals(menteeStatusTimestamp, engagementStatus.getMenteeStatusTimestamp());
        assertEquals(completedEngStatusTimestamp, engagementStatus.getCompletedEngStatusTimestamp());
    }

    @Test
    void testMentorEngStatusSetter() {
        EngagementStatus engagementStatus = new EngagementStatus();
        EngStatus mentorEngStatus = EngStatus.PENDING;

        engagementStatus.setMentorEngStatus(mentorEngStatus);

        assertEquals(mentorEngStatus, engagementStatus.getMentorEngStatus());
        assertNotNull(engagementStatus.getMentorStatusTimestamp());
    }

    @Test
    void testMenteeEngStatusSetter() {
        EngagementStatus engagementStatus = new EngagementStatus();
        EngStatus menteeEngStatus = EngStatus.PENDING;

        engagementStatus.setMenteeEngStatus(menteeEngStatus);

        assertEquals(menteeEngStatus, engagementStatus.getMenteeEngStatus());
        assertNotNull(engagementStatus.getMenteeStatusTimestamp());
    }
    // Test the NoArgsConstructor

    @Test
    void testNoArgsConstructor() {
        EngagementStatus engagementStatus = new EngagementStatus();

        assertNull(engagementStatus.getEngagementStatusId());
        assertNull(engagementStatus.getEngagement());
        assertNull(engagementStatus.getMentorEngStatus());
        assertNull(engagementStatus.getMenteeEngStatus());
        assertNull(engagementStatus.getMentorStatusTimestamp());
        assertNull(engagementStatus.getMenteeStatusTimestamp());
        assertNull(engagementStatus.getCompletedEngStatusTimestamp());
    }
}
