package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.dto.*;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.*;
import com.nineleaps.authentication.jwt.service.interfaces.ILoggingAndReportingService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class LoggingAndReportingServiceImpl implements ILoggingAndReportingService {
    @Autowired
    private ConnectionRequestRepo connectionRequestRepo;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoalTrackerRepository goalTrackerRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private EngagementRepository engagementRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoggingAndReportingServiceImpl.class);

    public static final String CATEGORY_USER_METRICS = "User Metrics";
    public static final String CATEGORY_CONNECTION_METRICS = "Connection Metrics";

    @Override
    public ConnectionRequestStatisticsMentorDTO getConnectionsStatisticsByMentorId(Long mentorId) {
        // Call the necessary methods to get connection statistics
        int total = getConnectionsReceivedByMentor(mentorId);
        int accepted = getConnectionsAcceptedByMentor(mentorId);
        int rejected = getConnectionsRejectedByMentor(mentorId);
        int pending = getConnectionsPendingMentor(mentorId);

        return new ConnectionRequestStatisticsMentorDTO(total, accepted, rejected, pending);
    }

    @Override
    public SlotStatisticsDTO getSlotStatisticsByMentorId(Long mentorId) {
        // Call the necessary methods to get slot statistics
        int totalSlots = getTotalSlotsByMentorId(mentorId);
        int bookedSlots = getBookedSlotsByMentorId(mentorId);
        int pendingSlots = getPendingSlotsByMentorId(mentorId);

        return new SlotStatisticsDTO(totalSlots, bookedSlots, pendingSlots);
    }

    private <T> T countAndLog(String entityName, Supplier<T> countSupplier) {
        logger.info("Counting {}...", entityName);
        return countSupplier.get();
    }

    public long countMentors() {
        return countAndLog("mentors", () -> userRepository.countUsersByRole(UserRole.MENTOR));
    }

    public long countMentees() {
        return countAndLog("mentees", () -> userRepository.countUsersByRole(UserRole.MENTEE));
    }

    public int countGoalsByEngagement(Long engagementId) {
        return countAndLog("goals for engagement with ID: " + engagementId, () -> goalTrackerRepository.countGoalsByEngagement(engagementId));
    }


    //        ________________________________________________________________________
    //Slot count
    //    Total slot a mentor have
    public int getTotalSlotsByMentorId(Long mentorId) {
        logger.info("Counting total slots for mentor with ID: {}", mentorId);
        return slotRepository.getTotalSlotsByMentorId(mentorId);
    }

    public int getBookedSlotsByMentorId(Long mentorId) {
        logger.info("Counting booked slots for mentor with ID: {}", mentorId);
        return slotRepository.getBookedSlotsByMentorId(mentorId);
    }

    public int getPendingSlotsByMentorId(Long mentorId) {
        logger.info("Counting pending slots for mentor with ID: {}", mentorId);
        return slotRepository.getPendingSlotsByMentorId(mentorId);
    }

    public int getConnectionsReceivedByMentor(Long mentorId) {
        logger.info("Counting connection requests received by mentor with ID: {}", mentorId);
        return connectionRequestRepo.countConnectionRequestsReceivedByMentor(mentorId);
    }

    public int getConnectionsAcceptedByMentor(Long mentorId) {
        logger.info("Counting connection requests accepted by mentor with ID: {}", mentorId);
        return connectionRequestRepo.countConnectionRequestsAcceptedByMentor(mentorId);
    }

    public int getConnectionsRejectedByMentor(Long mentorId) {
        logger.info("Counting connection requests rejected by mentor with ID: {}", mentorId);
        return connectionRequestRepo.countConnectionRequestsRejectedByMentor(mentorId);
    }

    public int getConnectionsPendingMentor(Long mentorId) {
        logger.info("Counting pending connection requests for mentor with ID: {}", mentorId);
        return connectionRequestRepo.countConnectionRequestsPendingByMentor(mentorId);
    }

    //Connection requests Total/accepted/pending/rejected
    public ConnectionRequestStatisticsReportDTO getConnectionRequestStatistics() {
        logger.info("Getting connection request statistics.");
        return connectionRequestRepo.getConnectionRequestStatistics();
    }

    public UserStatsDTO getRegisteredUsersCount() {
        logger.info("Getting registered users count.");
        return userRepository.getRegisteredUsersCount();
    }

    public EngagementStatisticsDTO getEngagementSummary() {
        logger.info("Getting engagement summary.");
        return engagementRepository.getEngagementSummary();
    }

    public FeedbackAnalyticsDTO getFeedbackAnalytics() {
        logger.info("Getting feedback analytics.");
        return feedbackRepository.getFeedbackAnalytics();
    }


}