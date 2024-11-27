package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class MenteeFeedbackDTOTest {

    @Test
    void testNoArgsConstructor() {
        MenteeFeedbackDTO menteeFeedbackDTO = new MenteeFeedbackDTO();

        assertNull(menteeFeedbackDTO.getId());
        assertNull(menteeFeedbackDTO.getMentorRating());
        assertNull(menteeFeedbackDTO.getMentorFeedback());
        assertNull(menteeFeedbackDTO.getEngagementRating());
        assertNull(menteeFeedbackDTO.getEngagementFeedback());
        assertNull(menteeFeedbackDTO.getFeedbackFromUserName());
        assertNull(menteeFeedbackDTO.getCreatedTime());
    }

    @Test
    void testGettersAndSetters() {
        MenteeFeedbackDTO menteeFeedbackDTO = new MenteeFeedbackDTO();

        Long id = 1L;
        Double mentorRating = 4.5;
        String mentorFeedback = "Mentor feedback text";
        Double engagementRating = 4.2;
        String engagementFeedback = "Engagement feedback text";
        String feedbackFromUserName = "MenteeUser";
        LocalDateTime createdTime = LocalDateTime.now();

        menteeFeedbackDTO.setId(id);
        menteeFeedbackDTO.setMentorRating(mentorRating);
        menteeFeedbackDTO.setMentorFeedback(mentorFeedback);
        menteeFeedbackDTO.setEngagementRating(engagementRating);
        menteeFeedbackDTO.setEngagementFeedback(engagementFeedback);
        menteeFeedbackDTO.setFeedbackFromUserName(feedbackFromUserName);
        menteeFeedbackDTO.setCreatedTime(createdTime);

        assertEquals(id, menteeFeedbackDTO.getId());
        assertEquals(mentorRating, menteeFeedbackDTO.getMentorRating());
        assertEquals(mentorFeedback, menteeFeedbackDTO.getMentorFeedback());
        assertEquals(engagementRating, menteeFeedbackDTO.getEngagementRating());
        assertEquals(engagementFeedback, menteeFeedbackDTO.getEngagementFeedback());
        assertEquals(feedbackFromUserName, menteeFeedbackDTO.getFeedbackFromUserName());
        assertEquals(createdTime, menteeFeedbackDTO.getCreatedTime());
    }
}
