package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import com.nineleaps.authentication.jwt.exception.ConflictException;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.interfaces.ISlotService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/slots")
public class SlotController {

    private final ISlotService iSlotService;
    private final Logger logger = LoggerFactory.getLogger(SlotController.class);


    /**
     * Get all the uploaded slots of a Mentor by Mentor ID.
     *
     * @param mentorId   The ID of the mentor.
     * @param pageNumber The page number for pagination (default is 0).
     * @param pageSize   The number of slots per page (default is 10).
     * @return A list of mentor slots.
     */
    @GetMapping("/mentors/{mentorId}/slots")
//    @PreAuthorize("hasAnyAuthority('MENTOR')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Get all the Uploaded slots of a Mentor by Mentor id")
    public ResponseEntity<List<SlotDTO>> getSlotsByMentorId(
            @RequestParam Long mentorId,
            @RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<SlotDTO> slotsPage = iSlotService.getSlotsByMentorId(mentorId, pageNumber, pageSize);
        List<SlotDTO> slots = slotsPage.getContent(); // Extract content from Page
        return ResponseEntity.ok(slots);
    }

    /**
     * Upload the slots for your Mentee to Book.
     *
     * @param slotDTO The details of the slot to be created.
     * @return A response entity indicating the result of the slot creation process.
     * @throws ConflictException if there is a conflict while creating the slot.
     */
    @PostMapping("/createTheSlots")
    @ApiOperation("Upload the slots for your Mentee to Book")
    public ResponseEntity<Object> createSlot(@RequestBody SlotDTO slotDTO) throws ConflictException {
        slotDTO.setStatus(BookingStatus.PENDING); // Set the initial status as PENDING
        SlotDTO createdSlot = iSlotService.createSlot(slotDTO);

        if (createdSlot == null) {
            logger.error("Failed to create a slot");
            return ResponseHandler.error("Failed to create the slot.", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            logger.info("Slot created successfully with ID: {}", createdSlot.getId());
            return ResponseHandler.success("Slot created successfully", HttpStatus.CREATED, createdSlot);

        }
    }

    /**
     * Update the slot details by Slot id.
     *
     * @param id      The ID of the slot to be updated.
     * @param slotDTO The updated details of the slot.
     * @return A response entity indicating the result of the slot update process.
     * @throws ResourceNotFoundException if the slot with the given ID is not found.
     */
    @PutMapping("/updateSlotById")
    @ApiOperation("Update the slot details by Slot id")
    public ResponseEntity<Object> updateSlot(@RequestParam Long id, @RequestBody SlotDTO slotDTO) throws ResourceNotFoundException {
        SlotDTO updatedSlot = iSlotService.updateSlot(id, slotDTO);

        if (updatedSlot == null) {
            logger.error("No slot found with the given ID: {}", id);
            return ResponseHandler.error("No slot found with the given ID.", HttpStatus.NOT_FOUND);

        } else {
            logger.info("Slot updated successfully with ID: {}", id);
            return ResponseHandler.success("Slot updated successfully", HttpStatus.OK, updatedSlot);

        }
    }

    /**
     * Delete the slot by Slot id.
     *
     * @param id The ID of the slot to be deleted.
     * @return A response entity indicating the result of the slot deletion process.
     */
    @DeleteMapping("/deleteById")
    @ApiOperation("Delete the slot by Slot id")
    public ResponseEntity<Object> deleteSlot(@RequestParam Long id) {
        try {
            iSlotService.deleteSlot(id);
            logger.info("Slot deleted successfully with ID: {}", id);
            return ResponseHandler.success("Slot deleted successfully", HttpStatus.OK, null);
        } catch (ResourceNotFoundException e) {
            logger.error("No slot found with the given ID: {}", id);
            return ResponseHandler.error("No slot found with the given ID.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete multiple slots by Slot id.
     *
     * @param slotId The list of slot IDs to be deleted.
     * @return A response entity indicating the result of the slot deletion process.
     */
    @DeleteMapping("/deleteMultipleSlots")
    @ApiOperation("Delete multiple slot by Slot id")
    public ResponseEntity<Object> deleteBySlotId(@RequestParam List<Long> slotId) {
        try {
            iSlotService.deleteMultipleSlotsById(slotId);
            logger.info("Slots deleted successfully");
            return ResponseHandler.success("Slots Deleted", HttpStatus.OK, null);
        } catch (Exception e) {
            logger.error("Failed to delete slots: {}", e.getMessage());
            return ResponseHandler.error("Failed to delete slots.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get slot counts by mentor and date range.
     *
     * @param mentorId     The ID of the mentor.
     * @param startDateStr The start date of the date range.
     * @param endDateStr   The end date of the date range.
     * @return A response entity containing slot counts based on mentor and date range.
     */
    @GetMapping("/counts")
    @ApiOperation("Get slot counts by mentor and date range")
    public ResponseEntity<Object> getSlotCountsByMentorAndDateRange(
            @RequestParam("mentorId") Long mentorId,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr
    ) {
        SlotStatisticsDTO slotStatistics = iSlotService.getSlotCountsByMentorAndDateRange(mentorId, startDateStr, endDateStr);
        if (slotStatistics == null) {
            logger.error("Failed to retrieve slot counts for Mentor ID: {}", mentorId);
            return ResponseHandler.error("Failed to retrieve slot counts.", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            logger.info("Slot counts retrieved successfully for Mentor ID: {}", mentorId);
            return ResponseHandler.success("Slot counts retrieved successfully", HttpStatus.OK, slotStatistics);

        }
    }

}

