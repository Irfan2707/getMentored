package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsMentorDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.interfaces.ILoggingAndReportingService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/")
public class LoggingAndReportingController {

    private final ILoggingAndReportingService iloggingAndReportingService;
    private final Logger logger = LoggerFactory.getLogger(LoggingAndReportingController.class);

    /**
     * Endpoint to retrieve Connection Request Statistics for a Mentor by Mentor Id.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve statistics.
     * @return A response entity containing Connection Request Statistics.
     */
    @GetMapping("/connections/received/accepted/pending/rejectedByMentor/mentorId")
    @ApiOperation("Get Connection Request Statistics including the Accepted, Rejected, and Pending connections of a Mentor by Mentor Id")
    public ResponseEntity<Object> getConnectionsStatisticsByMentorId(@RequestParam Long mentorId) {
        ConnectionRequestStatisticsMentorDTO statistics = iloggingAndReportingService.getConnectionsStatisticsByMentorId(mentorId);

        if (statistics.getReceived() >= 0) {
            logger.info("Connection Request Statistics retrieved successfully for Mentor ID: {}", mentorId);
            return ResponseHandler.success("Connection Request Statistics retrieved successfully.", HttpStatus.OK, statistics);
        } else {
            logger.error("Failed to retrieve Connection Request Statistics for Mentor ID: {}", mentorId);
            return ResponseHandler.error("Failed to retrieve Connection Request Statistics.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to retrieve Slot Statistics for a Mentor by Mentor Id.
     *
     * @param mentorId The ID of the Mentor for whom to retrieve statistics.
     * @return A response entity containing Slot Statistics.
     */
    @GetMapping("/mentorId/countOfSlots/pending/booked")
    @ApiOperation("Get the count of Total number of Slots with their Status as Pending and Booked")
    public ResponseEntity<Object> getSlotStatisticsByMentorId(@RequestParam Long mentorId) {
        SlotStatisticsDTO statistics = iloggingAndReportingService.getSlotStatisticsByMentorId(mentorId);

        if (statistics.getTotalSlots() >= 0) {
            logger.info("Slot Statistics retrieved successfully for Mentor ID: {}", mentorId);
            return ResponseHandler.success("Slot Statistics retrieved successfully.", HttpStatus.OK, statistics);
        } else {
            logger.error("Failed to retrieve Slot Statistics for Mentor ID: {}", mentorId);
            return ResponseHandler.error("Failed to retrieve Slot Statistics.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
