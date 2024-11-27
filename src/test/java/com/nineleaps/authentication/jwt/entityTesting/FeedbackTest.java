package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Feedback;
import com.nineleaps.authentication.jwt.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FeedbackTest {

    @Test
    void testFeedbackAttributesAndRelationships() {
        Long id = 1L;
        User mentee = new User();
        User mentor = new User();
        Engagement engagement = new Engagement();
        Double mentorRating = 4.5;
        Double menteeRating = 4.0;
        String mentorFeedback = "Good mentorship!";
        String menteeFeedback = "Great engagement!";
        Double engagementRating = 4.2;
        String engagementFeedback = "Positive experience";
        String feedbackFromUserName = "testUser";
        LocalDateTime createdTime = LocalDateTime.now();

        Feedback feedback = new Feedback(id, mentee, mentor, engagement, mentorRating, menteeRating,
                mentorFeedback, menteeFeedback, engagementRating, engagementFeedback, feedbackFromUserName, createdTime);

        assertEquals(id, feedback.getId());
        assertEquals(mentee, feedback.getMentee());
        assertEquals(mentor, feedback.getMentor());
        assertEquals(engagement, feedback.getEngagement());
        assertEquals(mentorRating, feedback.getMentorRating());
        assertEquals(menteeRating, feedback.getMenteeRating());
        assertEquals(mentorFeedback, feedback.getMentorFeedback());
        assertEquals(menteeFeedback, feedback.getMenteeFeedback());
        assertEquals(engagementRating, feedback.getEngagementRating());
        assertEquals(engagementFeedback, feedback.getEngagementFeedback());
        assertEquals(feedbackFromUserName, feedback.getFeedbackFromUserName());
        assertEquals(createdTime, feedback.getCreatedTime());
    }

    @Test
    void testNoArgsConstructor() {
        // Test the NoArgsConstructor
        Feedback feedback = new Feedback();

        assertNull(feedback.getId());
        assertNull(feedback.getMentee());
        assertNull(feedback.getMentor());
        assertNull(feedback.getEngagement());
        assertNull(feedback.getMentorRating());
        assertNull(feedback.getMenteeRating());
        assertNull(feedback.getMentorFeedback());
        assertNull(feedback.getMenteeFeedback());
        assertNull(feedback.getEngagementRating());
        assertNull(feedback.getEngagementFeedback());
        assertNull(feedback.getFeedbackFromUserName());
        assertNull(feedback.getCreatedTime());
    }
}
