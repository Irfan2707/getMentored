package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import com.nineleaps.authentication.jwt.entity.ConnectionRequest;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.exception.DuplicateEngagementException;
import com.nineleaps.authentication.jwt.repository.ConnectionRequestRepo;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.service.interfaces.IEngagementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class EngagementServiceImpl implements IEngagementService {
    @Autowired
    private final EngagementRepository engagementRepository;
    @Autowired
    private final ConnectionRequestRepo connectionRequestRepository;
    @Autowired
    private final EmailServiceImpl emailService;

    private static final Logger logger = LoggerFactory.getLogger(EngagementServiceImpl.class);

    @Override
    public ResponseEntity<CreateEngagementRequestDTO> createEngagement(
            @Valid CreateEngagementRequestDTO createEngagementRequest
    ) throws DuplicateEngagementException {
        Long connectionRequestId = createEngagementRequest.getConnectionRequest().getId();
        LocalDateTime startTime = createEngagementRequest.getStartTime();
        int durationHours = createEngagementRequest.getDurationHours();

        ConnectionRequest connectionRequest = connectionRequestRepository.findById(connectionRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found"));

        if (engagementRepository.existsByConnectionRequestId(connectionRequestId)) {
            throw new DuplicateEngagementException("Engagement already exists with the same start time, duration, and connection request");
        }

        Engagement engagement = new Engagement();
        engagement.setConnectionRequest(connectionRequest);
        engagement.setStartTime(startTime);
        engagement.setDurationHours(durationHours);

        Engagement savedEngagement = engagementRepository.save(engagement);

        // Retrieve the mentee's email
        String menteeEmail = savedEngagement.getConnectionRequest().getMentee().getUserMail();
        String mentorName = savedEngagement.getConnectionRequest().getMentor().getUserName();

        // Compose email subject and content
        String subject = "Engagement Confirmation";
        String content = "Your Mentor " + mentorName + " has carefully evaluated the "
                + "goals and objectives, and the time required to cover the relevant topics. The estimated duration of " + durationHours + " hours will allow for comprehensive guidance and "
                + "support so that you can make the most of this mentorship opportunity. \n Happy Learning!!!! ";

        emailService.sendEmail(subject, content, menteeEmail);
        logger.info("Engagement created successfully. Engagement ID: {}", savedEngagement.getId());

        createEngagementRequest.setId(savedEngagement.getId());
        CreateEngagementRequestDTO engagementDTO = CreateEngagementRequestDTO.fromEngagement(savedEngagement);
        return ResponseEntity.ok(engagementDTO);
    }

    @Override
    public List<Map<String, Object>> getEngagementDetailsByUserId(Long userId) {

        return engagementRepository.findEngagementDetailsByUserId(userId);
    }


}