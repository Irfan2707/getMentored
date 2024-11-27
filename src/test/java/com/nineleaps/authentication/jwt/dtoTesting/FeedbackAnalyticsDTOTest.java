package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.FeedbackAnalyticsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class FeedbackAnalyticsDTOTest {

    @Test
    void testNoArgsConstructor() {
        FeedbackAnalyticsDTO feedbackDTO = new FeedbackAnalyticsDTO();

        assertNull(feedbackDTO.getAverageEngagementRating());
        assertNull(feedbackDTO.getAverageMenteeRating());
        assertNull(feedbackDTO.getAverageMentorRating());
        assertNull(feedbackDTO.getPositiveFeedbackCount());
        assertNull(feedbackDTO.getNegativeFeedbackCount());
        assertNull(feedbackDTO.getNeutralFeedbackCount());
    }

    @Test
    void testAllArgsConstructor() {
        Double averageEngagementRating = 4.5;
        Double averageMenteeRating = 3.8;
        Double averageMentorRating = 4.2;
        Long positiveFeedbackCount = 15L;
        Long negativeFeedbackCount = 5L;
        Long neutralFeedbackCount = 10L;

        FeedbackAnalyticsDTO feedbackDTO = new FeedbackAnalyticsDTO(
                averageEngagementRating,
                averageMenteeRating,
                averageMentorRating,
                positiveFeedbackCount,
                negativeFeedbackCount,
                neutralFeedbackCount
        );

        assertEquals(averageEngagementRating, feedbackDTO.getAverageEngagementRating());
        assertEquals(averageMenteeRating, feedbackDTO.getAverageMenteeRating());
        assertEquals(averageMentorRating, feedbackDTO.getAverageMentorRating());
        assertEquals(positiveFeedbackCount, feedbackDTO.getPositiveFeedbackCount());
        assertEquals(negativeFeedbackCount, feedbackDTO.getNegativeFeedbackCount());
        assertEquals(neutralFeedbackCount, feedbackDTO.getNeutralFeedbackCount());
    }

    @Test
    void testGettersAndSetters() {
        FeedbackAnalyticsDTO feedbackDTO = new FeedbackAnalyticsDTO();

        Double averageEngagementRating = 4.5;
        Double averageMenteeRating = 3.8;
        Double averageMentorRating = 4.2;
        Long positiveFeedbackCount = 15L;
        Long negativeFeedbackCount = 5L;
        Long neutralFeedbackCount = 10L;

        feedbackDTO.setAverageEngagementRating(averageEngagementRating);
        feedbackDTO.setAverageMenteeRating(averageMenteeRating);
        feedbackDTO.setAverageMentorRating(averageMentorRating);
        feedbackDTO.setPositiveFeedbackCount(positiveFeedbackCount);
        feedbackDTO.setNegativeFeedbackCount(negativeFeedbackCount);
        feedbackDTO.setNeutralFeedbackCount(neutralFeedbackCount);

        assertEquals(averageEngagementRating, feedbackDTO.getAverageEngagementRating());
        assertEquals(averageMenteeRating, feedbackDTO.getAverageMenteeRating());
        assertEquals(averageMentorRating, feedbackDTO.getAverageMentorRating());
        assertEquals(positiveFeedbackCount, feedbackDTO.getPositiveFeedbackCount());
        assertEquals(negativeFeedbackCount, feedbackDTO.getNegativeFeedbackCount());
        assertEquals(neutralFeedbackCount, feedbackDTO.getNeutralFeedbackCount());
    }
}
