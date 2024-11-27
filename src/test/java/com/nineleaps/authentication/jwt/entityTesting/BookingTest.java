package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Slot;
import com.nineleaps.authentication.jwt.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BookingTest {

//    @Test
//    void testAllArgsConstructor() {
//        Long id = 1L;
//        LocalDateTime bookingDateTime = LocalDateTime.now();
//        Slot slot = new Slot();
//        LocalDateTime createdAt = LocalDateTime.now();
//        User mentee = new User();
//        User mentor = new User();
//        Engagement engagement = new Engagement();
//        Integer noOfHours = 2;
//        Long version = 1L;
//
//        Booking booking = new Booking(id, bookingDateTime, slot, createdAt, mentee, mentor, engagement, noOfHours, version);
//
//        assertEquals(id, booking.getId());
//        assertEquals(bookingDateTime, booking.getBookingDateTime());
//        assertEquals(slot, booking.getSlot());
//        assertEquals(createdAt, booking.getCreatedAt());
//        assertEquals(mentee, booking.getMentee());
//        assertEquals(mentor, booking.getMentor());
//        assertEquals(engagement, booking.getEngagement());
//        assertEquals(noOfHours, booking.getNoOfHours());
//        assertEquals(version, booking.getVersion());
//    }

    @Test
    void testNoArgsConstructor() {
        Booking booking = new Booking();

        assertNull(booking.getId());
        assertNull(booking.getBookingDateTime());
        assertNull(booking.getSlot());
        assertNull(booking.getCreatedAt());
        assertNull(booking.getMentee());
        assertNull(booking.getMentor());
        assertNull(booking.getEngagement());
        assertNull(booking.getNoOfHours());
        assertNull(booking.getVersion());
    }

    @Test
    void testGetterAndSetter() {
        Long id = 1L;
        LocalDateTime bookingDateTime = LocalDateTime.now();
        Slot slot = new Slot();
        LocalDateTime createdAt = LocalDateTime.now();
        User mentee = new User();
        User mentor = new User();
        Engagement engagement = new Engagement();
        Integer noOfHours = 2;
        Long version = 1L;

        Booking booking = new Booking();
        booking.setId(id);
        booking.setBookingDateTime(bookingDateTime);
        booking.setSlot(slot);
        booking.setCreatedAt(createdAt);
        booking.setMentee(mentee);
        booking.setMentor(mentor);
        booking.setEngagement(engagement);
        booking.setNoOfHours(noOfHours);
        booking.setVersion(version);

        assertEquals(id, booking.getId());
        assertEquals(bookingDateTime, booking.getBookingDateTime());
        assertEquals(slot, booking.getSlot());
        assertEquals(createdAt, booking.getCreatedAt());
        assertEquals(mentee, booking.getMentee());
        assertEquals(mentor, booking.getMentor());
        assertEquals(engagement, booking.getEngagement());
        assertEquals(noOfHours, booking.getNoOfHours());
        assertEquals(version, booking.getVersion());
    }
}
