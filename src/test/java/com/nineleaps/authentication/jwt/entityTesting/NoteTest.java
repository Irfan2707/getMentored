package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Note;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NoteTest {

    @Test
    void testNoteAttributesAndRelationships() {
        Long id = 1L;
        String title = "Important Note";
        String content = "This is the content of the note.";
        User user = new User();
        NoteVisibility visibility = NoteVisibility.PUBLIC;
        Engagement engagement = new Engagement();
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();
        boolean is_deleted=false;

        Note note = new Note(id, title, content, user, visibility, engagement, createdTime, updatedTime,is_deleted);

        assertEquals(id, note.getId());
        assertEquals(title, note.getTitle());
        assertEquals(content, note.getContent());
        assertEquals(user, note.getUser());
        assertEquals(visibility, note.getVisibility());
        assertEquals(engagement, note.getEngagement());
        assertEquals(createdTime, note.getCreatedTime());
        assertEquals(updatedTime, note.getUpdatedTime());
    }

    @Test
    void testNoArgsConstructor() {
        Note note = new Note();

        assertNull(note.getId());
        assertNull(note.getTitle());
        assertNull(note.getContent());
        assertNull(note.getUser());
        assertNull(note.getVisibility());
        assertNull(note.getEngagement());
        assertNull(note.getCreatedTime());
        assertNull(note.getUpdatedTime());
    }
}
