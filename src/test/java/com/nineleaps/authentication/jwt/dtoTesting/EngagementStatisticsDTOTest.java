package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.EngagementStatisticsDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class EngagementStatisticsDTOTest {

    @Test
    void testConstructorAndGetters() {
        EngagementStatisticsDTO statisticsDTO = new EngagementStatisticsDTO(10L, 100L, 10.5);

        assertEquals(10L, statisticsDTO.getTotalEngagements());
        assertEquals(100L, statisticsDTO.getTotalHours());
        assertEquals(10.5, statisticsDTO.getAverageHours(), 0.01);
    }

    @Test
    void testNoArgsConstructor() {
        EngagementStatisticsDTO statisticsDTO = new EngagementStatisticsDTO();

        assertNull(statisticsDTO.getTotalEngagements());
        assertNull(statisticsDTO.getTotalHours());
        assertNull(statisticsDTO.getAverageHours());
    }

    @Test
    void testSetters() {
        EngagementStatisticsDTO statisticsDTO = new EngagementStatisticsDTO();

        statisticsDTO.setTotalEngagements(20L);
        statisticsDTO.setTotalHours(200L);
        statisticsDTO.setAverageHours(15.75);

        assertEquals(20L, statisticsDTO.getTotalEngagements());
        assertEquals(200L, statisticsDTO.getTotalHours());
        assertEquals(15.75, statisticsDTO.getAverageHours(), 0.01);
    }
}
