package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class MentorFeedbackDTOTest {

    @Test
    void testNoArgsConstructor() {
        MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();

        assertNull(mentorFeedbackDTO.getId());
        assertNull(mentorFeedbackDTO.getMenteeRating());
        assertNull(mentorFeedbackDTO.getMenteeFeedback());
        assertNull(mentorFeedbackDTO.getEngagementRating());
        assertNull(mentorFeedbackDTO.getEngagementFeedback());
        assertNull(mentorFeedbackDTO.getFeedbackFromUserName());
        assertNull(mentorFeedbackDTO.getCreatedTime());
    }

    @Test
    void testGettersAndSetters() {
        MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();

        Long id = 1L;
        Double menteeRating = 4.5;
        String menteeFeedback = "Mentee feedback text";
        Double engagementRating = 4.2;
        String engagementFeedback = "Engagement feedback text";
        String feedbackFromUserName = "MentorUser";
        LocalDateTime createdTime = LocalDateTime.now();

        mentorFeedbackDTO.setId(id);
        mentorFeedbackDTO.setMenteeRating(menteeRating);
        mentorFeedbackDTO.setMenteeFeedback(menteeFeedback);
        mentorFeedbackDTO.setEngagementRating(engagementRating);
        mentorFeedbackDTO.setEngagementFeedback(engagementFeedback);
        mentorFeedbackDTO.setFeedbackFromUserName(feedbackFromUserName);
        mentorFeedbackDTO.setCreatedTime(createdTime);

        assertEquals(id, mentorFeedbackDTO.getId());
        assertEquals(menteeRating, mentorFeedbackDTO.getMenteeRating());
        assertEquals(menteeFeedback, mentorFeedbackDTO.getMenteeFeedback());
        assertEquals(engagementRating, mentorFeedbackDTO.getEngagementRating());
        assertEquals(engagementFeedback, mentorFeedbackDTO.getEngagementFeedback());
        assertEquals(feedbackFromUserName, mentorFeedbackDTO.getFeedbackFromUserName());
        assertEquals(createdTime, mentorFeedbackDTO.getCreatedTime());
    }
}
