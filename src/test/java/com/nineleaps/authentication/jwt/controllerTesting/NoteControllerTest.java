package com.nineleaps.authentication.jwt.controllerTesting;

import com.nineleaps.authentication.jwt.controller.NoteController;
import com.nineleaps.authentication.jwt.dto.NoteDTO;
import com.nineleaps.authentication.jwt.service.implementation.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class NoteControllerTest {
    @InjectMocks
    private NoteController noteController;
    @Mock
    private NoteService noteService;
    @Mock
    private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateNoteSuccess() {
        // Arrange
        NoteDTO privateNoteDTO = new NoteDTO();
        privateNoteDTO.setTitle("Test Note");
        privateNoteDTO.setContent("This is a test note.");

        NoteDTO noteResponseDTO = new NoteDTO();
        noteResponseDTO.setId(1L);
        noteResponseDTO.setTitle("Test Note");
        noteResponseDTO.setContent("This is a test note.");

        when(noteService.createNote(privateNoteDTO)).thenReturn(noteResponseDTO);

        // Act
        ResponseEntity<Object> responseEntity = noteController.createNote(privateNoteDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Note created successfully.", responseBody.get("message"));
        NoteDTO responseNoteDTO = (NoteDTO) responseBody.get("data");
        assertNotNull(responseNoteDTO);
        assertEquals(1L, responseNoteDTO.getId());
        assertEquals("Test Note", responseNoteDTO.getTitle());
        assertEquals("This is a test note.", responseNoteDTO.getContent());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testCreateNoteFailure() {
        // Arrange
        NoteDTO privateNoteDTO = new NoteDTO();
        privateNoteDTO.setTitle("Test Note");
        privateNoteDTO.setContent("This is a test note.");

        when(noteService.createNote(privateNoteDTO)).thenReturn(null); // Simulate failure

        // Act
        ResponseEntity<Object> responseEntity = noteController.createNote(privateNoteDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to create note.", responseBody.get("error"));


    }


    @Test
    void testUpdateNoteSuccess() {
        // Arrange
        Long noteId = 1L;
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Updated Note");
        noteDTO.setContent("This is an updated note.");

        NoteDTO updatedNote = new NoteDTO();
        updatedNote.setId(noteId);
        updatedNote.setTitle("Updated Note");
        updatedNote.setContent("This is an updated note.");

        when(noteService.updateNote(noteId, noteDTO)).thenReturn(updatedNote);

        // Act
        ResponseEntity<Object> responseEntity = noteController.updateNote(noteId, noteDTO);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Note updated successfully.", responseBody.get("message"));
        NoteDTO responseNoteDTO = (NoteDTO) responseBody.get("data");
        assertNotNull(responseNoteDTO);
        assertEquals(noteId, responseNoteDTO.getId());
        assertEquals("Updated Note", responseNoteDTO.getTitle());
        assertEquals("This is an updated note.", responseNoteDTO.getContent());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testUpdateNoteFailure() {
        // Arrange
        Long noteId = 1L;
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Updated Note");
        noteDTO.setContent("This is an updated note.");

        when(noteService.updateNote(noteId, noteDTO)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = noteController.updateNote(noteId, noteDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Failed to update note.", responseBody.get("error"));

    }


    @Test
    void testDeleteNoteSuccess() {
        // Arrange
        Long noteId = 1L;

        // Act
        ResponseEntity<Object> responseEntity = noteController.deleteNote(noteId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Note deleted successfully.", responseBody.get("message"));

        verify(logger, never()).error(anyString());
    }


    @Test
    void testGetPublicNotesByEngagementIdSuccess() {
        // Arrange
        Long engagementId = 1L;
        List<NoteDTO> publicNotes = new ArrayList<>();
        publicNotes.add(new NoteDTO());
        publicNotes.add(new NoteDTO());

        when(noteService.getPublicNotesByEngagementId(engagementId)).thenReturn(publicNotes);

        // Act
        ResponseEntity<Object> responseEntity = noteController.getPublicNotesByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Public notes retrieved successfully.", responseBody.get("message"));
        List<NoteDTO> notes = (List<NoteDTO>) responseBody.get("data");
        assertNotNull(notes);
        assertEquals(publicNotes.size(), notes.size());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetPublicNotesByEngagementIdNoNotesFound() {
        // Arrange
        Long engagementId = 2L;

        when(noteService.getPublicNotesByEngagementId(engagementId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = noteController.getPublicNotesByEngagementId(engagementId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No public notes found for the given Engagement Id.", responseBody.get("error"));

    }


    @Test
    void testGetPrivateNotesByUserIdAndEngagementIdSuccess() {
        // Arrange
        Long userId = 1L;
        Long engagementId = 2L;
        List<NoteDTO> privateNotes = new ArrayList<>();
        privateNotes.add(new NoteDTO());
        privateNotes.add(new NoteDTO());

        when(noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId)).thenReturn(privateNotes);

        // Act
        ResponseEntity<Object> responseEntity = noteController.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("Private notes retrieved successfully.", responseBody.get("message"));
        List<NoteDTO> notes = (List<NoteDTO>) responseBody.get("data");
        assertNotNull(notes);
        assertEquals(privateNotes.size(), notes.size());

        verify(logger, never()).error(anyString());
    }

    @Test
    void testGetPrivateNotesByUserIdAndEngagementIdNoNotesFound() {
        // Arrange
        Long userId = 3L;
        Long engagementId = 4L;

        when(noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId)).thenReturn(null);

        // Act
        ResponseEntity<Object> responseEntity = noteController.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

        assertTrue(responseEntity.getBody() instanceof Map);
        Map<String, Object> responseBody = (Map<String, Object>) responseEntity.getBody();
        assertNotNull(responseBody);

        assertEquals("No private notes found for the given User Id and Engagement Id.", responseBody.get("error"));


    }


}