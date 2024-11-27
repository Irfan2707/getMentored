package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.SlotDTO;
import com.nineleaps.authentication.jwt.enums.BookingStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class SlotDTOTest {

    @Test
    void testNoArgsConstructor() {
        SlotDTO slotDTO = new SlotDTO();

        assertNull(slotDTO.getId());
        assertNull(slotDTO.getStartDateTime());
        assertNull(slotDTO.getEndDateTime());
        assertNull(slotDTO.getMentorId());
        assertNull(slotDTO.getStatus());
    }

    @Test
    void testGettersAndSetters() {
        SlotDTO slotDTO = new SlotDTO();

        Long id = 1L;
        LocalDateTime startDateTime = LocalDateTime.now();
        LocalDateTime endDateTime = startDateTime.plusHours(1);
        Long mentorId = 101L;
        BookingStatus status = BookingStatus.BOOKED;

        slotDTO.setId(id);
        slotDTO.setStartDateTime(startDateTime);
        slotDTO.setEndDateTime(endDateTime);
        slotDTO.setMentorId(mentorId);
        slotDTO.setStatus(status);

        assertEquals(id, slotDTO.getId());
        assertEquals(startDateTime, slotDTO.getStartDateTime());
        assertEquals(endDateTime, slotDTO.getEndDateTime());
        assertEquals(mentorId, slotDTO.getMentorId());
        assertEquals(status, slotDTO.getStatus());
    }
}
