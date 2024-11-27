package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ConnectionRequestStatisticsMentorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConnectionRequestStatisticsMentorDTOTest {

    private ConnectionRequestStatisticsMentorDTO connectionRequestStatisticsMentorDTO;

    @BeforeEach
    public void setUp() {
        connectionRequestStatisticsMentorDTO = new ConnectionRequestStatisticsMentorDTO(5, 3, 1, 2);
    }


    @Test
    void testSetReceived() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 0, 0);

        dto.setReceived(3);

        assertEquals(3, dto.getReceived());
    }

    @Test
    void testGetAccepted() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 3, 0, 0);

        assertEquals(3, dto.getAccepted());
    }

    @Test
    void testSetAccepted() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 0, 0);

        dto.setAccepted(2);

        assertEquals(2, dto.getAccepted());
    }

    @Test
    void testGetRejected() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 1, 0);

        assertEquals(1, dto.getRejected());
    }

    @Test
    void testSetRejected() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 0, 0);

        dto.setRejected(4);

        assertEquals(4, dto.getRejected());
    }

    @Test
    void testGetPending() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 0, 2);

        assertEquals(2, dto.getPending());
    }

    @Test
    void testSetPending() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO(0, 0, 0, 0);

        dto.setPending(6);

        assertEquals(6, dto.getPending());
    }

    @Test
    void testNoArgsConstructor() {
        ConnectionRequestStatisticsMentorDTO dto = new ConnectionRequestStatisticsMentorDTO();

        assertNotNull(dto);
    }

}
