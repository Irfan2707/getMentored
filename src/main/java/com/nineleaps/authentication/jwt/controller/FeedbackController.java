package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.FeedbackServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/feedBacks")
public class FeedbackController {

    private final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    private final FeedbackServiceImpl feedbackServiceImpl;

    public static final String FEEDBACK_CREATED_SUCCESSFULLY = "Feedback created successfully.";
    public static final String FAILED_TO_CREATE_FEEDBACK = "Failed to create feedback.";
    public static final String FEEDBACK_RETRIEVED_SUCCESSFULLY = "All feedbacks retrieved successfully.";
    public static final String NO_FEEDBACKS_FOUND_WARNING_MENTOR = "No feedbacks found for the given mentor.";
    public static final String NO_FEEDBACKS_FOUND_WARNING_MENTEE = "No feedbacks found for the given mentee.";

    public static final String AVG_RATING_RETRIEVED_SUCCESSFULLY = "Average feedback rating retrieved successfully.";

    /**
     * Endpoint for a Mentee to give feedback to their Mentor.
     *
     * @param menteeId     The ID of the Mentee giving feedback.
     * @param mentorId     The ID of the Mentor receiving feedback.
     * @param engagementId The ID of the engagement for which feedback is given.
     * @param feedbackDTO  The request body containing Mentee's feedback information.
     * @return A response entity indicating the result of the feedback creation and the created feedback if successful.
     */
    @PostMapping("/menteeFeedbackToMentor")
    @ApiOperation("Mentee gives feedback to his Mentor")
    public ResponseEntity<Object> createFeedbackByMentee(
            @RequestParam Long menteeId,
            @RequestParam Long mentorId,
            @RequestParam Long engagementId,
            @RequestBody MenteeFeedbackDTO feedbackDTO) {
        // Create feedback by Mentee to Mentor
        MenteeFeedbackDTO createdFeedback = feedbackServiceImpl.createFeedbackByMentee(menteeId, mentorId, engagementId, feedbackDTO);

        if (createdFeedback != null) {
            logger.info(FEEDBACK_CREATED_SUCCESSFULLY);
            return ResponseHandler.success(FEEDBACK_CREATED_SUCCESSFULLY, HttpStatus.OK, createdFeedback);
        } else {
            logger.error(FAILED_TO_CREATE_FEEDBACK);
            return ResponseHandler.error(FAILED_TO_CREATE_FEEDBACK, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for a Mentor to give feedback to their Mentee.
     *
     * @param mentorId     The ID of the Mentor giving feedback.
     * @param menteeId     The ID of the Mentee receiving feedback.
     * @param engagementId The ID of the engagement for which feedback is given.
     * @param feedbackDTO  The request body containing Mentor's feedback information.
     * @return A response entity indicating the result of the feedback creation and the created feedback if successful.
     */
    @PostMapping("/mentorFeedbackToMentee")
    @ApiOperation("Mentor gives feedback to his Mentee")
    public ResponseEntity<Object> createFeedbackByMentor(
            @RequestParam Long mentorId,
            @RequestParam Long menteeId,
            @RequestParam Long engagementId,
            @RequestBody MentorFeedbackDTO feedbackDTO) {
        // Create feedback by Mentor to Mentee
        MentorFeedbackDTO createdFeedbackMentor = feedbackServiceImpl.createFeedbackByMentor(mentorId, menteeId, engagementId, feedbackDTO);

        if (createdFeedbackMentor != null) {
            logger.info(FEEDBACK_CREATED_SUCCESSFULLY);
            return ResponseHandler.success(FEEDBACK_CREATED_SUCCESSFULLY, HttpStatus.OK, createdFeedbackMentor);
        } else {
            logger.error(FAILED_TO_CREATE_FEEDBACK);
            return ResponseHandler.error(FAILED_TO_CREATE_FEEDBACK, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving all feedbacks given by Mentee to Mentor.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve feedbacks.
     * @return A response entity containing the list of Mentee feedbacks or an error message if not found.
     */
    @GetMapping("/getAllFeedbackByMenteeToMentor")
    @ApiOperation("Get All feedbacks by Mentee to Mentor")
    public ResponseEntity<Object> getAllFeedbackByMentee(@RequestParam Long mentorId) {
        // Get all feedbacks given by Mentee to Mentor
        List<MenteeFeedbackDTO> feedbackDTOs = feedbackServiceImpl.getAllFeedbackByMentee(mentorId);

        if (feedbackDTOs != null) {
            logger.info(FEEDBACK_RETRIEVED_SUCCESSFULLY);
            return ResponseHandler.success(FEEDBACK_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, feedbackDTOs);
        } else {
            logger.warn(NO_FEEDBACKS_FOUND_WARNING_MENTOR);
            return ResponseHandler.error(NO_FEEDBACKS_FOUND_WARNING_MENTOR, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving all feedbacks given by Mentor to Mentee.
     *
     * @param menteeId The ID of the Mentee for whom to retrieve feedbacks.
     * @return A response entity containing the list of Mentor feedbacks or an error message if not found.
     */
    @GetMapping("/getAllFeedbackByMentorToMentee")
    @ApiOperation("Get All feedbacks by Mentor to Mentee")
    public ResponseEntity<Object> getAllFeedbackByMentor(@RequestParam Long menteeId) {
        // Get all feedbacks given by Mentor to Mentee
        List<MentorFeedbackDTO> feedbackDTOs = feedbackServiceImpl.getAllFeedbackByMentor(menteeId);

        if (feedbackDTOs != null) {
            return ResponseHandler.success(FEEDBACK_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, feedbackDTOs);
        } else {
            return ResponseHandler.error(NO_FEEDBACKS_FOUND_WARNING_MENTEE, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving the average feedback rating for a Mentor by their ID.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve the average rating.
     * @return A response entity containing the average rating or an error message if not found.
     */
    @GetMapping("/getAvgMentorRatingByMentorId")
    @ApiOperation("Get Average Feedback Rating for a Mentor By his userId")
    public ResponseEntity<Object> getAvgMentorRating(@RequestParam Long mentorId) {
        // Get average feedback rating for a Mentor
        Double avgRating = feedbackServiceImpl.getAvgMentorRating(mentorId);

        if (avgRating != null) {
            return ResponseHandler.success(AVG_RATING_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, avgRating);
        } else {
            return ResponseHandler.error("Average feedback rating not found for the given mentor.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAvgMenteeRatingByMenteeId")
    @ApiOperation("Get Average Feedback Rating for a Mentee By his userId")
    public ResponseEntity<Object> getAvgMenteeRating(@RequestParam Long menteeId) {
        // Get average feedback rating for a Mentee
        Double avgRating = feedbackServiceImpl.getAvgMenteeRating(menteeId);

        if (avgRating != null) {
            return ResponseHandler.success(AVG_RATING_RETRIEVED_SUCCESSFULLY, HttpStatus.OK, avgRating);
        } else {
            return ResponseHandler.error("Average feedback rating not found for the given mentee.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for retrieving the average feedback rating for an Engagement by Mentor ID.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve the average rating.
     * @return A response entity containing the average rating or an error message if not found.
     */
    @GetMapping("/getAvgEngagementRatingByMentorId")
    @ApiOperation("Get Average Feedback Rating for Engagement By his userId")
    public ResponseEntity<Object> getAvgEngagementRating(@RequestParam Long mentorId) {
        // Get average feedback rating for an Engagement
        Double avgRating = feedbackServiceImpl.getAvgEngagementRating(mentorId);

        if (avgRating != null) {
            return ResponseHandler.success("Average feedback rating for engagement retrieved successfully.", HttpStatus.OK, avgRating);
        } else {
            return ResponseHandler.error("Average feedback rating for engagement not found for the given mentor.", HttpStatus.NOT_FOUND);
        }
    }
}
