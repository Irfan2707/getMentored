package com.nineleaps.authentication.jwt.controller;


import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.BookingDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.interfaces.IBookingService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookings")
public class BookingController {


    private final IBookingService iBookingService;
    private final Logger logger = LoggerFactory.getLogger(BookingController.class);

    /**
     * Endpoint for creating a booking with the provided booking details.
     *
     * @param bookingDTO The data transfer object containing booking details.
     * @return A response entity containing information about the created booking.
     */

    @PostMapping("/createBookings")
    @ApiOperation("Book the Slots of your Mentor")
    public ResponseEntity<Object> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO responseDTO = iBookingService.createBooking(bookingDTO);

        if (responseDTO != null) {
            logger.info("Booking created successfully. Booking ID: {}", responseDTO.getId());
            return ResponseHandler.success("Booking created successfully", HttpStatus.CREATED, responseDTO);
        } else {
            logger.error("Failed to create booking.");
            return ResponseHandler.error("Failed to create booking.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint for deleting multiple bookings identified by their respective IDs.
     *
     * @param bookingIds A list of booking IDs to be deleted.
     * @return A response entity indicating the result of the deletion operation.
     * @throws ResourceNotFoundException If any of the specified booking IDs are not found.
     */

    @DeleteMapping("/deleteMultipleBookings")
    @ApiOperation("Delete Multiple Bookings by ID")
    public ResponseEntity<Object> deleteMultipleBookings(@RequestParam List<Long> bookingIds) throws ResourceNotFoundException {
        iBookingService.deleteMultipleBookingsById(bookingIds);

        logger.info("Bookings deleted successfully. Booking IDs: {}", bookingIds);
        return ResponseHandler.success("Bookings deleted successfully", HttpStatus.OK, null);
    }

    /**
     * Endpoint for deleting a booking identified by its unique ID.
     *
     * @param id The ID of the booking to be deleted.
     * @return A response entity indicating the result of the deletion operation.
     * @throws ResourceNotFoundException If the specified booking ID is not found.
     */
    @DeleteMapping("/deleteById")
    @ApiOperation("Delete your Booking")
    public ResponseEntity<Object> deleteBooking(@RequestParam Long id) throws ResourceNotFoundException {
        iBookingService.deleteBooking(id);

        logger.info("Booking deleted successfully. Booking ID: {}", id);
        return ResponseHandler.success("Booking deleted successfully", HttpStatus.OK, null);
    }

    /**
     * Endpoint for retrieving booking details associated with a specific user ID.
     *
     * @param userId The ID of the user for whom booking details are to be retrieved.
     * @return A response entity containing the booking details for the specified user.
     */
    @GetMapping("/detailsByUserId")
    @ApiOperation("Get Booking Details By User Id")
    public ResponseEntity<Object> getBookingDetailsByUserId(@RequestParam("userId") Long userId) {
        List<Map<String, Object>> bookingDetails = iBookingService.getBookingDetailsByUserId(userId);

        if (!bookingDetails.isEmpty()) {
            logger.info("Booking details retrieved successfully for User ID: {}", userId);
            return ResponseHandler.success("Booking details retrieved successfully", HttpStatus.OK, bookingDetails);
        } else {
            logger.info("No booking details found for User ID: {}", userId);
            return ResponseHandler.error("No booking details found for the given user.", HttpStatus.NOT_FOUND);
        }
    }

}
