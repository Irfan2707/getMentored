package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class SlotStatisticsDTOTest {

    @Test
    void testNoArgsConstructor() {
        SlotStatisticsDTO slotStatisticsDTO = new SlotStatisticsDTO();

        assertEquals(0L, slotStatisticsDTO.getTotalSlots());
        assertEquals(0L, slotStatisticsDTO.getBookedSlots());
        assertEquals(0L, slotStatisticsDTO.getPendingSlots());
    }

    @Test
    void testAllArgsConstructor() {
        long totalSlots = 10L;
        long bookedSlots = 5L;
        long pendingSlots = 3L;

        SlotStatisticsDTO slotStatisticsDTO = new SlotStatisticsDTO(totalSlots, bookedSlots, pendingSlots);

        assertEquals(totalSlots, slotStatisticsDTO.getTotalSlots());
        assertEquals(bookedSlots, slotStatisticsDTO.getBookedSlots());
        assertEquals(pendingSlots, slotStatisticsDTO.getPendingSlots());
    }

    @Test
    void testGettersAndSetters() {
        SlotStatisticsDTO slotStatisticsDTO = new SlotStatisticsDTO();

        long totalSlots = 10L;
        long bookedSlots = 5L;
        long pendingSlots = 3L;

        slotStatisticsDTO.setTotalSlots(totalSlots);
        slotStatisticsDTO.setBookedSlots(bookedSlots);
        slotStatisticsDTO.setPendingSlots(pendingSlots);

        assertEquals(totalSlots, slotStatisticsDTO.getTotalSlots());
        assertEquals(bookedSlots, slotStatisticsDTO.getBookedSlots());
        assertEquals(pendingSlots, slotStatisticsDTO.getPendingSlots());
    }
}
