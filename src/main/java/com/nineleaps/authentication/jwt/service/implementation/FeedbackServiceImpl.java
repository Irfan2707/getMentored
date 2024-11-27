package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.EngagementNotFoundException;
import com.nineleaps.authentication.jwt.controllerexceptions.UserNotFoundException;
import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Feedback;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.exception.RedundantFeedbackException;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.FeedbackRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IFeedbackService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements IFeedbackService {
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final FeedbackRepository feedbackRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final EngagementRepository engagementRepository;
    private static final Logger logger = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    //creating feedback for mentor by mentee
    @Override
    public MenteeFeedbackDTO createFeedbackByMentee(Long menteeId, Long mentorId, Long engagementId, MenteeFeedbackDTO feedbackDTO) {

        logger.info("Creating feedback for Mentee ID: {}, Mentor ID: {}, Engagement ID: {}", menteeId, mentorId, engagementId);


        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new UserNotFoundException("Mentee not found with ID: " + menteeId));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new UserNotFoundException("Mentor not found with ID: " + mentorId));

        Engagement engagement = engagementRepository.findById(engagementId)
                .orElseThrow(() -> new EngagementNotFoundException("Engagement not found with ID: " + engagementId));

        if (feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementId(menteeId, mentorId, engagementId)) {
            logger.error("Feedback already submitted for the mentor and the engagement");
            throw new RedundantFeedbackException("Feedback already submitted for the mentor and engagement");
        }

        Feedback feedback = new Feedback();
        feedback.setMentee(mentee);
        feedback.setMentor(mentor);
        feedback.setEngagement(engagement);
        feedback.setCreatedTime(LocalDateTime.now());

        if (feedbackDTO.getMentorRating() != null && feedbackDTO.getMentorRating() >= 0 && feedbackDTO.getMentorRating() <= 5) {
            feedback.setMentorRating(feedbackDTO.getMentorRating());
        } else {
            logger.error("Invalid mentor rating. Rating must be followed between 0 and 5.");
            throw new IllegalArgumentException("Invalid mentor rating. Rating must be between 0 and 5.");
        }
        feedback.setMentorFeedback(feedbackDTO.getMentorFeedback());

        if (feedbackDTO.getEngagementRating() != null && feedbackDTO.getEngagementRating() >= 0 && feedbackDTO.getEngagementRating() <= 5) {
            feedback.setEngagementRating(feedbackDTO.getEngagementRating());
        } else {
            logger.error("Invalid engagement rating. Rating must be followed between 0 and 5.");
            throw new IllegalArgumentException("Invalid engagement rating. Rating must be between 0 and 5.");
        }
        feedback.setEngagementFeedback(feedbackDTO.getEngagementFeedback());
        feedback.setFeedbackFromUserName(feedbackDTO.getFeedbackFromUserName());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return modelMapper.map(savedFeedback, feedbackDTO.getClass());

    }

    //creating feedback for mentee by mentor
    @Override
    public MentorFeedbackDTO createFeedbackByMentor(Long mentorId, Long menteeId, Long engagementId, MentorFeedbackDTO mentorFeedbackDTO) {

        User mentee = userRepository.findById(menteeId)
                .orElseThrow(() -> new UserNotFoundException("Mentee not found with ID: " + menteeId));

        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new UserNotFoundException("Mentor not found with ID: " + mentorId));

        Engagement engagement = engagementRepository.findById(engagementId)
                .orElseThrow(() -> new EngagementNotFoundException("Engagement not found with ID: " + engagementId));


        if (feedbackRepository.existsByMenteeIdAndMentorIdAndEngagementIdAndMentorRating(menteeId, mentorId, engagementId, new Feedback().getMentorRating())) {
            throw new RedundantFeedbackException("Feedback already submitted for the mentor and engagement");
        }

        Feedback feedback = new Feedback();
        feedback.setMentee(mentee);
        feedback.setMentor(mentor);
        feedback.setEngagement(engagement);
        feedback.setCreatedTime(LocalDateTime.now());

        if (mentorFeedbackDTO.getMenteeRating() != null && mentorFeedbackDTO.getMenteeRating() >= 0 && mentorFeedbackDTO.getMenteeRating() <= 5) {
            feedback.setMenteeRating(mentorFeedbackDTO.getMenteeRating());
        } else {
            throw new IllegalArgumentException("Invalid mentor rating. Rating must be between 0 and 5.");
        }
        feedback.setMenteeFeedback(mentorFeedbackDTO.getMenteeFeedback());

        if (mentorFeedbackDTO.getEngagementRating() != null && mentorFeedbackDTO.getEngagementRating() >= 0 && mentorFeedbackDTO.getEngagementRating() <= 5) {
            feedback.setEngagementRating(mentorFeedbackDTO.getEngagementRating());
        } else {
            throw new IllegalArgumentException("Invalid engagement rating. Rating must be between 0 and 5.");
        }
        feedback.setEngagementFeedback(mentorFeedbackDTO.getEngagementFeedback());
        feedback.setFeedbackFromUserName(mentorFeedbackDTO.getFeedbackFromUserName());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return modelMapper.map(savedFeedback, mentorFeedbackDTO.getClass());

    }

    @Override
    public List<MenteeFeedbackDTO> getAllFeedbackByMentee(Long menteeId) {
        logger.info("Fetching all feedback by Mentee with ID: {}", menteeId);
        List<Feedback> feedbacks = feedbackRepository.findByMentorId(menteeId);
        logger.info("Fetched {} feedback records for Mentee with ID: {}", feedbacks.size(), menteeId);
        return feedbacks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<MentorFeedbackDTO> getAllFeedbackByMentor(Long mentorId) {
        logger.info("Fetching all feedback by Mentor with ID: {}", mentorId);
        List<Feedback> feedbacks = feedbackRepository.findByMenteeId(mentorId);
        logger.info("Fetched {} feedback records for Mentor with ID: {}", feedbacks.size(), mentorId);
        return feedbacks.stream().map(this::convertToDTOO).collect(Collectors.toList());
    }

    public MentorFeedbackDTO convertToDTOO(Feedback feedback) {
        return modelMapper.map(feedback, MentorFeedbackDTO.class);
    }

    public MenteeFeedbackDTO convertToDTO(Feedback feedback) {
        return modelMapper.map(feedback, MenteeFeedbackDTO.class);
    }


    public Double getAvgMentorRating(Long mentorId) {
        return feedbackRepository.calculateAverageRatingForMentorId(mentorId);
    }

    public Double getAvgMenteeRating(Long menteeId) {
        return feedbackRepository.calculateAverageRatingForMenteeId(menteeId);
    }

    public Double getAvgEngagementRating(Long mentorId) {
        return feedbackRepository.calculateAverageRatingForEngagement(mentorId);
    }
}