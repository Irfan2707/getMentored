package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.EngagementStatusDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.EngagementStatus;
import com.nineleaps.authentication.jwt.enums.EngStatus;
import com.nineleaps.authentication.jwt.repository.EngagementStatusRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EngagementStatusServiceImpl {
    @Autowired
    private final EngagementStatusRepository engagementStatusRepository;
    @Autowired
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(EngagementStatusServiceImpl.class);

    public ResponseEntity<EngagementStatusDTO> createOrUpdateEngagementStatus(Long engagementId, EngagementStatusDTO engagementStatusDTO) {
        engagementStatusDTO.setEngagementId(engagementId);
        EngagementStatusDTO result = processEngagementStatus(engagementStatusDTO);

        if (result == null) {
            logger.error("Failed to update engagement status for Engagement ID: {}", engagementId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new EngagementStatusDTO());
        }

        logger.info("Engagement status updated successfully. Engagement ID: {}", engagementId);

        return ResponseEntity.ok(result);
    }

    public EngagementStatusDTO processEngagementStatus(EngagementStatusDTO engagementStatusDTO) {
        Long engagementId = engagementStatusDTO.getEngagementId();
        Optional<EngagementStatus> engagementStatusOptional = engagementStatusRepository.findByEngagementId(engagementId);

        if (engagementStatusOptional.isPresent()) {
            EngagementStatus existingEngagementStatus = engagementStatusOptional.get();

            existingEngagementStatus.setMentorEngStatus(engagementStatusDTO.getMentorEngStatus());
            existingEngagementStatus.setMenteeEngStatus(engagementStatusDTO.getMenteeEngStatus());
            logger.info("Engagement status updated for Engagement ID: {}", engagementId);

            LocalDateTime mentorStatusTimestamp = existingEngagementStatus.getMentorStatusTimestamp();
            LocalDateTime menteeStatusTimestamp = existingEngagementStatus.getMenteeStatusTimestamp();

            if (mentorStatusTimestamp != null && menteeStatusTimestamp != null) {
                LocalDateTime latestStatusTimestamp = mentorStatusTimestamp.isAfter(menteeStatusTimestamp)
                        ? mentorStatusTimestamp
                        : menteeStatusTimestamp;

                if (existingEngagementStatus.getMentorEngStatus() == EngStatus.DONE &&
                        existingEngagementStatus.getMenteeEngStatus() == EngStatus.DONE) {
                    existingEngagementStatus.setCompletedEngStatusTimestamp(latestStatusTimestamp); // Set the completion timestamp
                } else {
                    existingEngagementStatus.setCompletedEngStatusTimestamp(null);
                }
            }


            EngagementStatus updatedEngagementStatus = engagementStatusRepository.save(existingEngagementStatus);
            return modelMapper.map(updatedEngagementStatus, EngagementStatusDTO.class);
        } else {
            Engagement engagement = new Engagement();
            engagement.setId(engagementStatusDTO.getEngagementId());

            EngagementStatus engagementStatus = new EngagementStatus();
            engagementStatus.setEngagement(engagement); // Initialize engagementStatus with engagement
            // Set other properties on engagementStatus
            logger.info("Engagement status created for Engagement ID: {}", engagementId);

            LocalDateTime currentTimestamp = LocalDateTime.now();
            engagementStatus.setMentorStatusTimestamp(currentTimestamp);
            engagementStatus.setMenteeStatusTimestamp(currentTimestamp);
            engagementStatus.setMentorEngStatus(EngStatus.PENDING);
            engagementStatus.setMenteeEngStatus(EngStatus.PENDING);
            engagementStatus.setCompletedEngStatusTimestamp(null); // Set default value

            EngagementStatus savedEngagementStatus = engagementStatusRepository.save(engagementStatus);
            return modelMapper.map(savedEngagementStatus, EngagementStatusDTO.class);
        }


    }

    public ResponseEntity<EngagementStatusDTO> getEngagementStatusByEngagementId(Long engagementId) {
        Optional<EngagementStatus> engagementStatusOptional = engagementStatusRepository.findByEngagementId(engagementId);
        if (engagementStatusOptional.isPresent()) {
            EngagementStatus engagementStatus = engagementStatusOptional.get();
            EngagementStatusDTO engagementStatusDTO = modelMapper.map(engagementStatus, EngagementStatusDTO.class);

            logger.info("Engagement status retrieved successfully for Engagement ID: {}", engagementId);

            return ResponseEntity.ok(engagementStatusDTO);
        } else {

            logger.warn("Engagement status not found for Engagement ID: {}", engagementId);

            return ResponseEntity.notFound().build();
        }
    }

}
