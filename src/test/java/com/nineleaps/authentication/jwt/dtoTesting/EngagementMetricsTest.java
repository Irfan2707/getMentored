package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.EngagementMetrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class EngagementMetricsTest {

    @Test
    void testConstructorAndGetters() {
        EngagementMetrics metrics = new EngagementMetrics(5L, 10L);

        assertEquals(5L, metrics.getPendingEngagements());
        assertEquals(10L, metrics.getDoneEngagements());
    }

    @Test
    void testNoArgsConstructor() {
        EngagementMetrics metrics = new EngagementMetrics();

        assertNull(metrics.getPendingEngagements());
        assertNull(metrics.getDoneEngagements());
    }

    @Test
    void testSetters() {
        EngagementMetrics metrics = new EngagementMetrics();

        metrics.setPendingEngagements(7L);
        metrics.setDoneEngagements(15L);

        assertEquals(7L, metrics.getPendingEngagements());
        assertEquals(15L, metrics.getDoneEngagements());
    }
}
