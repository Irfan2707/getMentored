package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.controllerexceptions.ResourceNotFoundException;
import com.nineleaps.authentication.jwt.dto.BookingDTO;

import java.util.List;
import java.util.Map;

public interface IBookingService {

    void deleteBooking(Long id) throws ResourceNotFoundException;


    BookingDTO createBooking(BookingDTO bookingDTO);

    void deleteMultipleBookingsById(List<Long> bookingIds) throws ResourceNotFoundException;

    List<Map<String, Object>> getBookingDetailsByUserId(Long userId);
}
