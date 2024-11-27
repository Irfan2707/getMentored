package com.nineleaps.authentication.jwt.dtoTesting;

import com.nineleaps.authentication.jwt.dto.NoteDTO;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

 class NoteDTOTest {

    @Test
    void testNoArgsConstructor() {
        NoteDTO noteDTO = new NoteDTO();

        assertNull(noteDTO.getUserId());
        assertNull(noteDTO.getId());
        assertNull(noteDTO.getTitle());
        assertNull(noteDTO.getContent());
        assertNull(noteDTO.getVisibility());
        assertNull(noteDTO.getEngagementId());
        assertNull(noteDTO.getCreatedTime());
        assertNull(noteDTO.getUpdatedTime());
    }

    @Test
    void testGettersAndSetters() {
        NoteDTO noteDTO = new NoteDTO();

        Long userId = 1L;
        Long id = 2L;
        String title = "Note Title";
        String content = "Note Content";
        NoteVisibility visibility = NoteVisibility.PUBLIC;
        Long engagementId = 3L;
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now().plusHours(1);

        noteDTO.setUserId(userId);
        noteDTO.setId(id);
        noteDTO.setTitle(title);
        noteDTO.setContent(content);
        noteDTO.setVisibility(visibility);
        noteDTO.setEngagementId(engagementId);
        noteDTO.setCreatedTime(createdTime);
        noteDTO.setUpdatedTime(updatedTime);

        assertEquals(userId, noteDTO.getUserId());
        assertEquals(id, noteDTO.getId());
        assertEquals(title, noteDTO.getTitle());
        assertEquals(content, noteDTO.getContent());
        assertEquals(visibility, noteDTO.getVisibility());
        assertEquals(engagementId, noteDTO.getEngagementId());
        assertEquals(createdTime, noteDTO.getCreatedTime());
        assertEquals(updatedTime, noteDTO.getUpdatedTime());
    }
}
