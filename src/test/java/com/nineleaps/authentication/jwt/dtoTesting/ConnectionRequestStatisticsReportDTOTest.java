package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsReportDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConnectionRequestStatisticsReportDTOTest {

    @Test
    void testGetTotalRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(10L, 0L, 0L, 0L);

        assertEquals(10L, dto.getTotalRequests());
    }

    @Test
    void testSetTotalRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 0L, 0L);

        dto.setTotalRequests(20L);

        assertEquals(20L, dto.getTotalRequests());
    }

    @Test
    void testGetAcceptedRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 5L, 0L, 0L);

        assertEquals(5L, dto.getAcceptedRequests());
    }

    @Test
    void testSetAcceptedRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 0L, 0L);

        dto.setAcceptedRequests(15L);

        assertEquals(15L, dto.getAcceptedRequests());
    }

    @Test
    void testGetRejectedRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 3L, 0L);

        assertEquals(3L, dto.getRejectedRequests());
    }

    @Test
    void testSetRejectedRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 0L, 0L);

        dto.setRejectedRequests(8L);

        assertEquals(8L, dto.getRejectedRequests());
    }

    @Test
    void testGetPendingRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 0L, 7L);

        assertEquals(7L, dto.getPendingRequests());
    }

    @Test
    void testSetPendingRequests() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO(0L, 0L, 0L, 0L);

        dto.setPendingRequests(12L);

        assertEquals(12L, dto.getPendingRequests());
    }

    @Test
    void testNoArgsConstructor() {
        ConnectionRequestStatisticsReportDTO dto = new ConnectionRequestStatisticsReportDTO();

        assertNotNull(dto);
    }
}