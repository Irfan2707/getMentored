package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.CreateEngagementRequestDTO;
import com.nineleaps.authentication.jwt.exception.DuplicateEngagementException;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.EngagementServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/engagements")
public class EngagementController {

    private final Logger logger = LoggerFactory.getLogger(EngagementController.class);


    private final EngagementServiceImpl engagementServiceImpl;


    /**
     * Endpoint for starting an engagement between a Mentor and a Mentee.
     *
     * @param createEngagementRequest The request body containing information to create the engagement.
     * @return A response entity indicating the result of the engagement creation.
     */
    @PostMapping("/createEngagement")
    @ApiOperation("Start an Engagement between Mentor and Mentee")
    public ResponseEntity<Object> createEngagement(@Valid @RequestBody CreateEngagementRequestDTO createEngagementRequest) {
        try {
            ResponseEntity<CreateEngagementRequestDTO> responseEntity = engagementServiceImpl.createEngagement(createEngagementRequest);
            logger.info("Engagement created successfully.");
            return ResponseHandler.success("Engagement created successfully.", HttpStatus.CREATED, responseEntity.getBody());
        } catch (DuplicateEngagementException e) {
            logger.error("Error while creating engagement: {}", e.getMessage());
            return ResponseHandler.error("Duplicate engagement found.", HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            logger.error("Error while creating engagement: {}", ex.getMessage());
            return ResponseHandler.error("Error while creating engagement.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Endpoint for retrieving engagement details for a user by their ID.
     *
     * @param userId The ID of the user for whom to retrieve engagement details.
     * @return A response entity containing the engagement details or an error message if not found.
     */

    @GetMapping("/getEngagementsByUserId")
    @ApiOperation("Get Engagement Details By User id")
    public ResponseEntity<Object> getEngagementDetailsByUserId(@RequestParam Long userId) {
        List<Map<String, Object>> engagementDetails = engagementServiceImpl.getEngagementDetailsByUserId(userId);

        if (engagementDetails == null) {

            logger.error("No engagements found for the given user ID.");
            return ResponseHandler.error("No engagements found for the given user ID.", HttpStatus.NOT_FOUND);

        } else {
            logger.info("Engagement details retrieved successfully.");

            return ResponseHandler.success("Engagement details retrieved successfully.", HttpStatus.OK, engagementDetails);
        }
    }
}



