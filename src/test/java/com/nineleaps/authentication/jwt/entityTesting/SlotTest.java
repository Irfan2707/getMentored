package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Booking;
import com.nineleaps.authentication.jwt.entity.Slot;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

//    @Test
//    void testSlotAttributesAndRelationships() {
//        Long id = 1L;
//        List<Booking> bookings = new ArrayList<>();
//        LocalDateTime startDateTime = LocalDateTime.now();
//        LocalDateTime endDateTime = LocalDateTime.now().plusHours(1);
//        Long mentorId = 2L;
//        BookingStatus status = BookingStatus.BOOKED;
//        LocalDateTime createdAt = LocalDateTime.now();
//
//        Slot slot = new Slot(id, bookings, startDateTime, endDateTime, mentorId, status, createdAt);
//
//        assertEquals(id, slot.getId());
//        assertEquals(bookings, slot.getBookings());
//        assertEquals(startDateTime, slot.getStartDateTime());
//        assertEquals(endDateTime, slot.getEndDateTime());
//        assertEquals(mentorId, slot.getMentorId());
//        assertEquals(status, slot.getStatus());
//        assertEquals(createdAt, slot.getCreatedAt());
//    }

    @Test
    void testAddBooking() {
        Slot slot = new Slot();
        Booking booking = new Booking();

        slot.addBooking(booking);

        List<Booking> bookings = slot.getBookings();
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertEquals(booking, bookings.get(0));
        assertEquals(slot, booking.getSlot());
    }

    @Test
    void testRemoveBooking() {
        Slot slot = new Slot();
        Booking booking = new Booking();
        slot.addBooking(booking);

        slot.removeBooking(booking);

        List<Booking> bookings = slot.getBookings();
        assertNotNull(bookings);
        assertTrue(bookings.isEmpty());
        assertNull(booking.getSlot());
    }
    // Test the NoArgsConstructor

    @Test
    void testNoArgsConstructor() {
        Slot slot = new Slot();

        assertNull(slot.getId());
        assertNotNull(slot.getBookings());
        assertTrue(slot.getBookings().isEmpty());
        assertNull(slot.getStartDateTime());
        assertNull(slot.getEndDateTime());
        assertNull(slot.getMentorId());
        assertNull(slot.getStatus());
        assertNull(slot.getCreatedAt());
    }
}
