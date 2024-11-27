package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.MenteeFeedbackDTO;
import com.nineleaps.authentication.jwt.dto.MentorFeedbackDTO;

import java.util.List;

public interface IFeedbackService {
    //creating feedback for mentor by mentee
    MenteeFeedbackDTO createFeedbackByMentee(Long menteeId, Long mentorId, Long engagementId, MenteeFeedbackDTO feedbackDTO);

    //creating feedback for mentee by mentor
    MentorFeedbackDTO createFeedbackByMentor(Long mentorId, Long menteeId, Long engagementId, MentorFeedbackDTO mentorFeedbackDTO);

    List<MenteeFeedbackDTO> getAllFeedbackByMentee(Long menteeId);

    List<MentorFeedbackDTO> getAllFeedbackByMentor(Long mentorId);
}
