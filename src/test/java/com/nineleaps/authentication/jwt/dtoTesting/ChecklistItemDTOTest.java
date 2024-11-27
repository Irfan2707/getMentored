package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.ChecklistItemDTO;
import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


 class ChecklistItemDTOTest {

    @Test
    void testChecklistItemDTOConstructorAndGetters() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO(
                1L,
                101L,
                "Item description",
                createdAt,
                updatedAt,
                ChecklistitemStatus.PENDING
        );

        assertEquals(1L, checklistItemDTO.getId());
        assertEquals(101L, checklistItemDTO.getGoalTrackerId());
        assertEquals("Item description", checklistItemDTO.getItemDescription());
        assertEquals(createdAt, checklistItemDTO.getCreatedAt());
        assertEquals(updatedAt, checklistItemDTO.getUpdatedAt());
        assertEquals(ChecklistitemStatus.PENDING, checklistItemDTO.getStatus());
    }

    @Test
    void testChecklistItemDTONoArgsConstructor() {
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();

        assertNotNull(checklistItemDTO);
        assertNull(checklistItemDTO.getId());
        assertNull(checklistItemDTO.getGoalTrackerId());
        assertNull(checklistItemDTO.getItemDescription());
        assertNull(checklistItemDTO.getCreatedAt());
        assertNull(checklistItemDTO.getUpdatedAt());
        assertNull(checklistItemDTO.getStatus());
    }

    @Test
    void testChecklistItemDTOSetters() {
        ChecklistItemDTO checklistItemDTO = new ChecklistItemDTO();

        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        LocalDateTime updatedAt = LocalDateTime.now().minusMinutes(30);

        checklistItemDTO.setId(2L);
        checklistItemDTO.setGoalTrackerId(202L);
        checklistItemDTO.setItemDescription("Updated description");
        checklistItemDTO.setCreatedAt(createdAt);
        checklistItemDTO.setUpdatedAt(updatedAt);
        checklistItemDTO.setStatus(ChecklistitemStatus.PENDING);

        assertEquals(2L, checklistItemDTO.getId());
        assertEquals(202L, checklistItemDTO.getGoalTrackerId());
        assertEquals("Updated description", checklistItemDTO.getItemDescription());
        assertEquals(createdAt, checklistItemDTO.getCreatedAt());
        assertEquals(updatedAt, checklistItemDTO.getUpdatedAt());
        assertEquals(ChecklistitemStatus.PENDING, checklistItemDTO.getStatus());
    }
}


