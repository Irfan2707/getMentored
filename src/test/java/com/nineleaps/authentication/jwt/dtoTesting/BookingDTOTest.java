package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.BookingDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

 class BookingDTOTest {

    @Test
    void testBookingDTOConstructorAndGetters() {
        LocalDateTime bookingDateTime = LocalDateTime.now();

        BookingDTO bookingDTO = new BookingDTO(1L, 101L, 201L, 301L, bookingDateTime, 401L, 2, "MenteeUser", "MentorUser");

        assertEquals(1L, bookingDTO.getId());
        assertEquals(101L, bookingDTO.getSlotId());
        assertEquals(201L, bookingDTO.getMenteeId());
        assertEquals(301L, bookingDTO.getMentorId());
        assertEquals(bookingDateTime, bookingDTO.getBookingDateTime());
        assertEquals(401L, bookingDTO.getEngagementId());
        assertEquals(2, bookingDTO.getNoOfHours());
        assertEquals("MenteeUser", bookingDTO.getMenteeUsername());
        assertEquals("MentorUser", bookingDTO.getMentorUsername());
    }

    @Test
    void testBookingDTONoArgsConstructor() {
        BookingDTO bookingDTO = new BookingDTO();

        assertNotNull(bookingDTO);
        assertNull(bookingDTO.getId());
        assertNull(bookingDTO.getSlotId());
        assertNull(bookingDTO.getMenteeId());
        assertNull(bookingDTO.getMentorId());
        assertNull(bookingDTO.getBookingDateTime());
        assertNull(bookingDTO.getEngagementId());
        assertNull(bookingDTO.getNoOfHours());
        assertNull(bookingDTO.getMenteeUsername());
        assertNull(bookingDTO.getMentorUsername());
    }

    @Test
    void testBookingDTOSetters() {
        BookingDTO bookingDTO = new BookingDTO();

        LocalDateTime bookingDateTime = LocalDateTime.now().minusHours(1);

        bookingDTO.setId(2L);
        bookingDTO.setSlotId(102L);
        bookingDTO.setMenteeId(202L);
        bookingDTO.setMentorId(302L);
        bookingDTO.setBookingDateTime(bookingDateTime);
        bookingDTO.setEngagementId(402L);
        bookingDTO.setNoOfHours(3);
        bookingDTO.setMenteeUsername("AnotherMentee");
        bookingDTO.setMentorUsername("AnotherMentor");

        assertEquals(2L, bookingDTO.getId());
        assertEquals(102L, bookingDTO.getSlotId());
        assertEquals(202L, bookingDTO.getMenteeId());
        assertEquals(302L, bookingDTO.getMentorId());
        assertEquals(bookingDateTime, bookingDTO.getBookingDateTime());
        assertEquals(402L, bookingDTO.getEngagementId());
        assertEquals(3, bookingDTO.getNoOfHours());
        assertEquals("AnotherMentee", bookingDTO.getMenteeUsername());
        assertEquals("AnotherMentor", bookingDTO.getMentorUsername());
    }
}
