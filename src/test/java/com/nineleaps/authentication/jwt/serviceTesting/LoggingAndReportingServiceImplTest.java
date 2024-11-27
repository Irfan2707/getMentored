package com.nineleaps.authentication.jwt.serviceTesting;


import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsReportDTO;
import com.nineleaps.authentication.jwt.dto.EngagementStatisticsDTO;
import com.nineleaps.authentication.jwt.dto.FeedbackAnalyticsDTO;
import com.nineleaps.authentication.jwt.dto.UserStatsDTO;
import com.nineleaps.authentication.jwt.enums.UserRole;
import com.nineleaps.authentication.jwt.repository.*;
import com.nineleaps.authentication.jwt.service.implementation.LoggingAndReportingServiceImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

 class LoggingAndReportingServiceImplTest {

    @InjectMocks
    private LoggingAndReportingServiceImpl loggingAndReportingServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalTrackerRepository goalTrackerRepository;

    @Mock
    private SlotRepository slotRepository;
    @Mock
    private EntityManager entityManager;

    @Mock
    private ConnectionRequestRepo connectionRequestRepo;
    @Mock
    private EngagementRepository engagementRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private PDDocument mockDocument;

    @Mock
    private PDPage mockPage;
    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCountMentors() {
        when(userRepository.countUsersByRole(UserRole.MENTOR)).thenReturn(5L);

        long mentorCount = loggingAndReportingServiceImpl.countMentors();

        assertEquals(5L, mentorCount);
        verify(userRepository, times(1)).countUsersByRole(UserRole.MENTOR);
    }

    @Test
     void testCountMentees() {
        when(userRepository.countUsersByRole(UserRole.MENTEE)).thenReturn(3L);

        long menteeCount = loggingAndReportingServiceImpl.countMentees();

        assertEquals(3L, menteeCount);
        verify(userRepository, times(1)).countUsersByRole(UserRole.MENTEE);
    }

    @Test
     void testCountGoalsByEngagement() {
        long engagementId = 1L;
        when(goalTrackerRepository.countGoalsByEngagement(engagementId)).thenReturn(10);

        int goalCount = loggingAndReportingServiceImpl.countGoalsByEngagement(engagementId);

        assertEquals(10, goalCount);
        verify(goalTrackerRepository, times(1)).countGoalsByEngagement(engagementId);
    }


    @Test
     void testGetTotalSlotsByMentorId() {
        long mentorId = 1L;
        when(slotRepository.getTotalSlotsByMentorId(mentorId)).thenReturn(5);

        int totalSlots = loggingAndReportingServiceImpl.getTotalSlotsByMentorId(mentorId);

        assertEquals(5, totalSlots);
        verify(slotRepository, times(1)).getTotalSlotsByMentorId(mentorId);
    }

    @Test
     void testGetBookedSlotsByMentorId() {
        long mentorId = 1L;
        when(slotRepository.getBookedSlotsByMentorId(mentorId)).thenReturn(3);

        int bookedSlots = loggingAndReportingServiceImpl.getBookedSlotsByMentorId(mentorId);

        assertEquals(3, bookedSlots);
        verify(slotRepository, times(1)).getBookedSlotsByMentorId(mentorId);
    }

    @Test
     void testGetPendingSlotsByMentorId() {
        long mentorId = 1L;
        when(slotRepository.getPendingSlotsByMentorId(mentorId)).thenReturn(2);

        int pendingSlots = loggingAndReportingServiceImpl.getPendingSlotsByMentorId(mentorId);

        assertEquals(2, pendingSlots);
        verify(slotRepository, times(1)).getPendingSlotsByMentorId(mentorId);
    }

    @Test
     void testGetConnectionsReceivedByMentor() {
        long mentorId = 1L;
        when(connectionRequestRepo.countConnectionRequestsReceivedByMentor(mentorId)).thenReturn(7);

        int connectionsReceived = loggingAndReportingServiceImpl.getConnectionsReceivedByMentor(mentorId);

        assertEquals(7, connectionsReceived);
        verify(connectionRequestRepo, times(1)).countConnectionRequestsReceivedByMentor(mentorId);
    }

    @Test
     void testGetConnectionsAcceptedByMentor() {
        long mentorId = 1L;
        when(connectionRequestRepo.countConnectionRequestsAcceptedByMentor(mentorId)).thenReturn(4);

        int connectionsAccepted = loggingAndReportingServiceImpl.getConnectionsAcceptedByMentor(mentorId);

        assertEquals(4, connectionsAccepted);
        verify(connectionRequestRepo, times(1)).countConnectionRequestsAcceptedByMentor(mentorId);
    }

    @Test
     void testGetConnectionsRejectedByMentor() {
        long mentorId = 1L;
        when(connectionRequestRepo.countConnectionRequestsRejectedByMentor(mentorId)).thenReturn(2);

        int connectionsRejected = loggingAndReportingServiceImpl.getConnectionsRejectedByMentor(mentorId);

        assertEquals(2, connectionsRejected);
        verify(connectionRequestRepo, times(1)).countConnectionRequestsRejectedByMentor(mentorId);
    }


    @Test
     void testGetConnectionsPendingMentor() {
        long mentorId = 1L;
        when(connectionRequestRepo.countConnectionRequestsPendingByMentor(mentorId)).thenReturn(3);

        int connectionsPending = loggingAndReportingServiceImpl.getConnectionsPendingMentor(mentorId);

        assertEquals(3, connectionsPending);
        verify(connectionRequestRepo, times(1)).countConnectionRequestsPendingByMentor(mentorId);
    }

    @Test
     void testGetConnectionRequestStatistics() {
        ConnectionRequestStatisticsReportDTO expectedReport = new ConnectionRequestStatisticsReportDTO(7L,3L,2L,2L);
        when(connectionRequestRepo.getConnectionRequestStatistics()).thenReturn(expectedReport);

        ConnectionRequestStatisticsReportDTO report = loggingAndReportingServiceImpl.getConnectionRequestStatistics();

        assertEquals(expectedReport, report);
        verify(connectionRequestRepo, times(1)).getConnectionRequestStatistics();
    }

    @Test
     void testGetRegisteredUsersCount() {
        UserStatsDTO expectedStats = new UserStatsDTO(5L,3L,2L);
        when(userRepository.getRegisteredUsersCount()).thenReturn(expectedStats);

        UserStatsDTO stats = loggingAndReportingServiceImpl.getRegisteredUsersCount();

        assertEquals(expectedStats, stats);
        verify(userRepository, times(1)).getRegisteredUsersCount();
    }

    @Test
     void testGetEngagementSummary() {
        EngagementStatisticsDTO expectedSummary = new EngagementStatisticsDTO(5L,3L,2.1);
        when(engagementRepository.getEngagementSummary()).thenReturn(expectedSummary);

        EngagementStatisticsDTO summary = loggingAndReportingServiceImpl.getEngagementSummary();

        assertEquals(expectedSummary, summary);
        verify(engagementRepository, times(1)).getEngagementSummary();
    }

    @Test
     void testGetFeedbackAnalytics() {
        FeedbackAnalyticsDTO expectedAnalytics = new FeedbackAnalyticsDTO(2.1,3.2,2.5,5L,2L,1L);
        when(feedbackRepository.getFeedbackAnalytics()).thenReturn(expectedAnalytics);

        FeedbackAnalyticsDTO analytics = loggingAndReportingServiceImpl.getFeedbackAnalytics();

        assertEquals(expectedAnalytics, analytics);
        verify(feedbackRepository, times(1)).getFeedbackAnalytics();
    }





}
