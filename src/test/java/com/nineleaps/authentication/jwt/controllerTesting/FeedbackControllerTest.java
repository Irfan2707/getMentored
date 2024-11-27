package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.FeedbackController;
import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;
import com.nineleaps.authentication.jwt.service.implementation.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nineleaps.authentication.jwt.controller.FeedbackController.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Add appropriate test annotations and imports

class FeedbackControllerTest {

    @InjectMocks
    private FeedbackController feedbackController;

    @Mock
    private FeedbackServiceImpl feedbackServiceImpl;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateFeedbackByMenteeSuccess() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();

        when(feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO))
                .thenReturn(feedbackDTO);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.createFeedbackByMentee(
                menteeId, mentorId, engagementId, feedbackDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FEEDBACK_CREATED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(feedbackDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateFeedbackByMenteeFailure() {
        // Arrange
        Long menteeId = 1L;
        Long mentorId = 2L;
        Long engagementId = 3L;
        MenteeFeedbackDTO feedbackDTO = new MenteeFeedbackDTO();

        when(feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO))
                .thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.createFeedbackByMentee(
                menteeId, mentorId, engagementId, feedbackDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FAILED_TO_CREATE_FEEDBACK, responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }


    @Test
    void testCreateFeedbackByMentorSuccess() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        Long engagementId = 3L;
        MentorFeedbackDTO feedbackDTO = new MentorFeedbackDTO();

        when(feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, feedbackDTO))
                .thenReturn(feedbackDTO);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.createFeedbackByMentor(
                mentorId, menteeId, engagementId, feedbackDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FEEDBACK_CREATED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(feedbackDTO, responseBody.get("data"));

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateFeedbackByMentorFailure() {
        // Arrange
        Long mentorId = 1L;
        Long menteeId = 2L;
        Long engagementId = 3L;
        MentorFeedbackDTO feedbackDTO = new MentorFeedbackDTO();

        when(feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, feedbackDTO))
                .thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.createFeedbackByMentor(
                mentorId, menteeId, engagementId, feedbackDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FAILED_TO_CREATE_FEEDBACK, responseBody.get("error"));

        verify(logger, never()).info(anyString());
    }


    @Test
    void testGetAllFeedbackByMenteeSuccess() {
        // Arrange
        Long mentorId = 1L;
        MenteeFeedbackDTO menteeFeedbackDTO = new MenteeFeedbackDTO();
        menteeFeedbackDTO.setId(2L);
        List<MenteeFeedbackDTO> feedbackDTOs = new ArrayList<>();
        feedbackDTOs.add(menteeFeedbackDTO);

        when(feedbackServiceImpl.getAllFeedbackByMentee(mentorId)).thenReturn(feedbackDTOs);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAllFeedbackByMentee(mentorId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FEEDBACK_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(feedbackDTOs, responseBody.get("data"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAllFeedbackByMenteeFailure() {
        // Arrange
        Long mentorId = 1L;

        when(feedbackServiceImpl.getAllFeedbackByMentee(mentorId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAllFeedbackByMentee(mentorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(NO_FEEDBACKS_FOUND_WARNING_MENTOR, responseBody.get("error"));

        verify(logger, never()).warn(anyString());
    }


    @Test
    void testGetAllFeedbackByMentorSuccess() {
        // Arrange
        Long menteeId = 1L;
        MentorFeedbackDTO mentorFeedbackDTO = new MentorFeedbackDTO();
        mentorFeedbackDTO.setId(2L);
        List<MentorFeedbackDTO> feedbackDTOs = new ArrayList<>();
        feedbackDTOs.add(mentorFeedbackDTO);

        when(feedbackServiceImpl.getAllFeedbackByMentor(menteeId)).thenReturn(feedbackDTOs);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAllFeedbackByMentor(menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(FEEDBACK_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(feedbackDTOs, responseBody.get("data"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAllFeedbackByMentorFailure() {
        // Arrange
        Long menteeId = 1L;

        when(feedbackServiceImpl.getAllFeedbackByMentor(menteeId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAllFeedbackByMentor(menteeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(NO_FEEDBACKS_FOUND_WARNING_MENTEE, responseBody.get("error"));

        verify(logger, never()).warn(anyString());
    }


    @Test
    void testGetAvgMentorRatingSuccess() {
        // Arrange
        Long mentorId = 1L;
        Double avgRating = 4.5; // Average rating

        when(feedbackServiceImpl.getAvgMentorRating(mentorId)).thenReturn(avgRating);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgMentorRating(mentorId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(AVG_RATING_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(avgRating, responseBody.get("data"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAvgMentorRatingFailure() {
        // Arrange
        Long mentorId = 1L;

        when(feedbackServiceImpl.getAvgMentorRating(mentorId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgMentorRating(mentorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Average feedback rating not found for the given mentor.", responseBody.get("error"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAvgMenteeRatingSuccess() {
        // Arrange
        Long menteeId = 1L;
        Double avgRating = 4.2; // Average rating

        when(feedbackServiceImpl.getAvgMenteeRating(menteeId)).thenReturn(avgRating);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgMenteeRating(menteeId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals(AVG_RATING_RETRIEVED_SUCCESSFULLY, responseBody.get("message"));
        assertEquals(avgRating, responseBody.get("data"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAvgMenteeRatingFailure() {
        // Arrange
        Long menteeId = 1L;

        when(feedbackServiceImpl.getAvgMenteeRating(menteeId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgMenteeRating(menteeId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Average feedback rating not found for the given mentee.", responseBody.get("error"));

        verify(logger, never()).warn(anyString());
    }


    @Test
    void testGetAvgEngagementRatingSuccess() {
        // Arrange
        Long mentorId = 1L;
        Double avgRating = 4.5; // Average rating

        when(feedbackServiceImpl.getAvgEngagementRating(mentorId)).thenReturn(avgRating);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgEngagementRating(mentorId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Average feedback rating for engagement retrieved successfully.", responseBody.get("message"));
        assertEquals(avgRating, responseBody.get("data"));

        verify(logger, never()).warn(anyString());
    }

    @Test
    void testGetAvgEngagementRatingFailure() {
        // Arrange
        Long mentorId = 1L;

        when(feedbackServiceImpl.getAvgEngagementRating(mentorId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = feedbackController.getAvgEngagementRating(mentorId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Average feedback rating for engagement not found for the given mentor.", responseBody.get("error"));

        verify(logger, never()).warn(anyString());
    }


}
