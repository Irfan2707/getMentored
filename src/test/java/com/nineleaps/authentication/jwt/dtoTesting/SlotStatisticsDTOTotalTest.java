package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.SlotStatisticsDTOTotal;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class SlotStatisticsDTOTotalTest {

    @Test
    void testNoArgsConstructor() {
        SlotStatisticsDTOTotal slotStatisticsDTOTotal = new SlotStatisticsDTOTotal();

        assertEquals(0, slotStatisticsDTOTotal.getTotalSlots());
        assertEquals(0, slotStatisticsDTOTotal.getBookedSlots());
        assertEquals(0, slotStatisticsDTOTotal.getPendingSlots());
    }

    @Test
    void testAllArgsConstructor() {
        int totalSlots = 10;
        int bookedSlots = 5;
        int pendingSlots = 3;

        SlotStatisticsDTOTotal slotStatisticsDTOTotal = new SlotStatisticsDTOTotal(totalSlots, bookedSlots, pendingSlots);

        assertEquals(totalSlots, slotStatisticsDTOTotal.getTotalSlots());
        assertEquals(bookedSlots, slotStatisticsDTOTotal.getBookedSlots());
        assertEquals(pendingSlots, slotStatisticsDTOTotal.getPendingSlots());
    }

    @Test
    void testGettersAndSetters() {
        SlotStatisticsDTOTotal slotStatisticsDTOTotal = new SlotStatisticsDTOTotal();

        int totalSlots = 10;
        int bookedSlots = 5;
        int pendingSlots = 3;

        slotStatisticsDTOTotal.setTotalSlots(totalSlots);
        slotStatisticsDTOTotal.setBookedSlots(bookedSlots);
        slotStatisticsDTOTotal.setPendingSlots(pendingSlots);

        assertEquals(totalSlots, slotStatisticsDTOTotal.getTotalSlots());
        assertEquals(bookedSlots, slotStatisticsDTOTotal.getBookedSlots());
        assertEquals(pendingSlots, slotStatisticsDTOTotal.getPendingSlots());
    }
}
