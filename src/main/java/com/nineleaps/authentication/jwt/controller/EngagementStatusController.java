package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.EngagementStatusDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.EngagementStatusServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/engagementStatus")
public class EngagementStatusController {

    private static final Logger logger = LoggerFactory.getLogger(EngagementStatusController.class);


    private final EngagementStatusServiceImpl engagementStatusServiceImpl;

    /**
     * Endpoint for creating or updating the engagement status by Engagement ID.
     *
     * @param engagementId        The ID of the engagement for which to create or update the status.
     * @param engagementStatusDTO The request body containing the engagement status information.
     * @return A response entity indicating the result of the operation and the updated engagement status if successful.
     */
    @PutMapping("/createOrUpdateEngagementStatus")
    @ApiOperation("Create or update Engagement Status by Engagement Id")
    public ResponseEntity<Object> createOrUpdateEngagementStatus(
            @RequestParam Long engagementId,
            @RequestBody EngagementStatusDTO engagementStatusDTO
    ) {
        // Create or update engagement status
        ResponseEntity<EngagementStatusDTO> result = engagementStatusServiceImpl.createOrUpdateEngagementStatus(engagementId, engagementStatusDTO);

        if (result.getStatusCode() == HttpStatus.OK) {
            EngagementStatusDTO updatedEngagementStatus = result.getBody();
            logger.info("Engagement status created or updated successfully.");
            return ResponseHandler.success("Engagement status created or updated successfully.", HttpStatus.OK, updatedEngagementStatus);
        } else {
            logger.error("Failed to create or update engagement status.");
            return ResponseHandler.error("Failed to create or update engagement status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for retrieving the engagement status by Engagement ID.
     *
     * @param engagementId The ID of the engagement for which to retrieve the status.
     * @return A response entity containing the engagement status or an error message if not found.
     */
    @GetMapping("/getById")
    @ApiOperation("Get Engagement Status by Engagement Id")
    public ResponseEntity<Object> getEngagementStatusByEngagementId(@RequestParam("id") Long engagementId) {
        // Get engagement status by engagement ID
        ResponseEntity<EngagementStatusDTO> result = engagementStatusServiceImpl.getEngagementStatusByEngagementId(engagementId);

        if (result.getStatusCode() == HttpStatus.OK) {
            EngagementStatusDTO engagementStatus = result.getBody();
            logger.info("Engagement status retrieved successfully.");
            return ResponseHandler.success("Engagement status retrieved successfully.", HttpStatus.OK, engagementStatus);
        } else {
            logger.error("Engagement status not found.");
            return ResponseHandler.error("Engagement status not found.", HttpStatus.NOT_FOUND);
        }
    }

}
