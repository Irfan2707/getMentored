//package com.nineleaps.authentication.jwt.serviceTesting;
//
//import com.nineleaps.authentication.jwt.controllerExceptions.EngagementNotFoundException;
//import com.nineleaps.authentication.jwt.controllerExceptions.UserNotFoundException;
//import com.nineleaps.authentication.jwt.dto.NoteDTO;
//import com.nineleaps.authentication.jwt.entity.Engagement;
//import com.nineleaps.authentication.jwt.entity.Note;
//import com.nineleaps.authentication.jwt.entity.User;
//import com.nineleaps.authentication.jwt.enums.NoteVisibility;
//import com.nineleaps.authentication.jwt.repository.EngagementRepository;
//import com.nineleaps.authentication.jwt.repository.NoteRepository;
//import com.nineleaps.authentication.jwt.repository.UserRepository;
//import com.nineleaps.authentication.jwt.service.implementation.NoteService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class NoteServiceTest {
//
//    @InjectMocks
//    private NoteServiceTest noteServiceTest;
//    @Mock
//    private NoteService noteService;
//
//    @Mock
//    private NoteRepository noteRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EngagementRepository engagementRepository;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCreateNote() {
//        // Arrange
//        User user = new User();
//        user.setId(1L);
//        Engagement engagement = new Engagement();
//        engagement.setId(2L);
//
//        NoteDTO noteDTO = new NoteDTO();
//        noteDTO.setTitle("Test Note");
//        noteDTO.setContent("Test Content");
//        noteDTO.setVisibility(NoteVisibility.PRIVATE);
//        noteDTO.setUserId(1L); // Set the user ID in NoteDTO
//        noteDTO.setEngagementId(2L); // Set the engagement ID in NoteDTO
//
//        // Mock the behavior of the repositories
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(engagementRepository.findById(2L)).thenReturn(Optional.of(engagement));
//
//        // Mock the behavior of noteRepository to return a saved Note
//        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
//            Note savedNote = invocation.getArgument(0);
//            savedNote.setId(3L); // Set an ID for the saved note
//            return savedNote;
//        });
//        // Mock the behavior of modelMapper to return a mapped NoteDTO when map is called
//        when(modelMapper.map(any(Note.class), eq(NoteDTO.class))).thenAnswer(invocation -> {
//            Note sourceNote = invocation.getArgument(0);
//            NoteDTO mappedDTO = new NoteDTO();
//            // Set properties in mappedDTO based on sourceNote
//            mappedDTO.setId(sourceNote.getId());
//            mappedDTO.setTitle(sourceNote.getTitle());
//            mappedDTO.setContent(sourceNote.getContent());
//            // Set other properties as needed
//            return mappedDTO;
//        });
//
//        // Act
//        NoteDTO createdNoteDTO = noteService.createNote(noteDTO);
//
//        // Assert
//        assertNotNull(createdNoteDTO);
//        // Add additional assertions based on the expected behavior of the createNote method
//    }
//
//
//
//
//    @Test
//    void testCreateNote_UserNotFoundException() {
//        // Arrange
//        NoteDTO privateNoteDTO = new NoteDTO();
//        privateNoteDTO.setUserId(1L);
//
//        // Configure userRepository mock to return an empty Optional when findById is called with 1L
//        when(userRepository.findById(1L)).thenThrow(new UserNotFoundException("User not found with ID: 1"));
//
//    }
//
//    @Test
//    void testCreateNote_EngagementNotFoundException() {
//        // Arrange
//        NoteDTO privateNoteDTO = new NoteDTO();
//        privateNoteDTO.setUserId(1L);
////        privateNoteDTO.setEngagementId(2L);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
//        when(engagementRepository.findById(2L)).thenThrow(EngagementNotFoundException.class);
//
//
//    }
//
//    @Test
//    void testUpdateNote_Success() {
//        // Arrange
//        Long noteId = 1L;
//        NoteDTO noteDTO = new NoteDTO();
//        noteDTO.setTitle("Updated Title");
//        noteDTO.setContent("Updated Content");
//        noteDTO.setVisibility(NoteVisibility.PUBLIC);
//
//        Note existingNote = new Note();
//        existingNote.setId(noteId);
//        existingNote.setTitle("Original Title");
//        existingNote.setContent("Original Content");
//        existingNote.setVisibility(NoteVisibility.PRIVATE);
//
//        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
//        when(userRepository.findById(anyLong())).thenReturn(Optional.empty()); // Mock user not found
//
//        // Act
//        NoteDTO updatedNoteDTO = noteService.updateNote(noteId, noteDTO);
//
//        // Assert
//        assertNotNull(updatedNoteDTO);
//        assertEquals(noteDTO.getTitle(), updatedNoteDTO.getTitle());
//        assertEquals(noteDTO.getContent(), updatedNoteDTO.getContent());
//        assertEquals(noteDTO.getVisibility(), updatedNoteDTO.getVisibility());
//        assertEquals(noteId, updatedNoteDTO.getId());
//    }
//
//    @Test
//    void testUpdateNote_NoteNotFound() {
//        // Arrange
//        Long noteId = 1L;
//        NoteDTO noteDTO = new NoteDTO();
//        noteDTO.setTitle("Updated Title");
//        noteDTO.setContent("Updated Content");
//        noteDTO.setVisibility(NoteVisibility.PUBLIC);
//
//        when(userRepository.findById(1L)).thenThrow(new UserNotFoundException("User not found with ID: 1"));
//
//
//    }
//
//    @Test
//    void testUpdateNote_UserNotFound() {
//        // Arrange
//        Long noteId = 1L;
//        Long userId = 2L;
//        NoteDTO noteDTO = new NoteDTO();
//        noteDTO.setUserId(userId); // Set user ID to trigger user not found scenario
//
//        Note existingNote = new Note();
//        existingNote.setId(noteId);
//
//        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Act & Assert
//        assertThrows(EntityNotFoundException.class, () -> noteService.updateNote(noteId, noteDTO));
//    }
//
//    @Test
//    void testGetPublicNotesByEngagementId_Success() {
//        // Arrange
//        Long engagementId = 1L;
//
//        List<Note> publicNotes = new ArrayList<>();
//        publicNotes.add(createNoteWithVisibility(NoteVisibility.PUBLIC));
//        publicNotes.add(createNoteWithVisibility(NoteVisibility.PUBLIC));
//
//        // Debug: Print the publicNotes for verification
//        publicNotes.forEach(note -> System.out.println("Note Visibility: " + note.getVisibility()));
//
//        when(noteRepository.findByEngagementIdAndVisibility(engagementId, NoteVisibility.PUBLIC)).thenReturn(publicNotes);
//
//        // Act
//        List<NoteDTO> publicNoteDTOs = noteService.getPublicNotesByEngagementId(engagementId);
//
//        // Debug: Print the publicNoteDTOs for verification
//        publicNoteDTOs.forEach(noteDTO -> System.out.println("NoteDTO Visibility: " + noteDTO.getVisibility()));
//
//        // Assert
//        assertNotNull(publicNoteDTOs);
//        assertEquals(publicNotes.size(), publicNoteDTOs.size());
//    }
//
//
//    @Test
//    void testGetPrivateNotesByUserIdAndEngagementId_Success() {
//        // Arrange
//        Long userId = 1L;
//        Long engagementId = 2L;
//
//        List<Note> privateNotes = new ArrayList<>();
//        privateNotes.add(createNoteWithVisibility(NoteVisibility.PRIVATE));
//        privateNotes.add(createNoteWithVisibility(NoteVisibility.PRIVATE));
//
//        when(noteRepository.findByUserIdAndEngagementIdAndVisibility(userId, engagementId, NoteVisibility.PRIVATE)).thenReturn(privateNotes);
//
//        // Act
//        List<NoteDTO> privateNoteDTOs = noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);
//
//        // Assert
//        assertNotNull(privateNoteDTOs);
//        assertEquals(privateNotes.size(), privateNoteDTOs.size());
//    }
//
//    private Note createNoteWithVisibility(NoteVisibility visibility) {
//        Note note = new Note();
//        note.setVisibility(visibility);
//        // Set other properties as needed
//        return note;
//    }
//
//}



package com.nineleaps.authentication.jwt.serviceTesting;

import com.nineleaps.authentication.jwt.dto.NoteDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Note;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.NoteRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.implementation.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class NoteServiceTest {
    @Mock
    private NoteRepository noteRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EngagementRepository engagementRepository;
    @InjectMocks
    private NoteService noteService;
    @Mock
    private Logger logger;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetPrivateNotesByUserIdAndEngagementId_Success() {
        // Arrange
        Long userId = 1L;
        Long engagementId = 2L;
        List<Note> privateNotes = createSamplePrivateNotes(userId, engagementId);
        when(noteRepository.findByUserIdAndEngagementIdAndVisibility(userId, engagementId, NoteVisibility.PRIVATE))
                .thenReturn(privateNotes);
        // Act
        List<NoteDTO> result = noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);
        // Assert
        assertNotNull(result);
        assertEquals(privateNotes.size(), result.size());
    }
    @Test
    void testGetPrivateNotesByUserIdAndEngagementId_EmptyList() {
        // Arrange
        Long userId = 1L;
        Long engagementId = 2L;
        when(noteRepository.findByUserIdAndEngagementIdAndVisibility(userId, engagementId, NoteVisibility.PRIVATE))
                .thenReturn(new ArrayList<>());
        // Act
        List<NoteDTO> result = noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    // Helper method to create sample private notes for testing
    private List<Note> createSamplePrivateNotes(Long userId, Long engagementId) {
        List<Note> privateNotes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Note note = new Note();
            note.setId((long) i);
            note.setTitle("Private Note " + i);
            note.setContent("Content " + i);
            note.setVisibility(NoteVisibility.PRIVATE);
            note.setCreatedTime(LocalDateTime.now().minusMinutes(i));
            privateNotes.add(note);
        }
        return privateNotes;
    }
    // Helper method to map Note to NoteDTO for testing
    private NoteDTO mapNoteToDTO(Note note) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setId(note.getId());
        noteDTO.setTitle(note.getTitle());
        noteDTO.setContent(note.getContent());
        noteDTO.setVisibility(note.getVisibility());
        noteDTO.setUserId(note.getUser() != null ? note.getUser().getId() : null);
        noteDTO.setEngagementId(note.getEngagement() != null ? note.getEngagement().getId() : null);
        noteDTO.setCreatedTime(note.getCreatedTime());
        noteDTO.setUpdatedTime(note.getUpdatedTime());
        return noteDTO;
    }
    @Test
    void testGetPublicNotesByEngagementId_Success() {
        // Arrange
        Long engagementId = 1L;
        List<Note> publicNotes = createPublicNotes(engagementId);
        when(noteRepository.findByEngagementIdAndVisibility(engagementId, NoteVisibility.PUBLIC))
                .thenReturn(publicNotes);
        // Act
        List<NoteDTO> result = noteService.getPublicNotesByEngagementId(engagementId);
        // Assert
        assertNotNull(result);
        assertEquals(publicNotes.size(), result.size());
    }
    @Test
    void testGetPublicNotesByEngagementId_NoPublicNotes() {
        // Arrange
        Long engagementId = 1L;
        when(noteRepository.findByEngagementIdAndVisibility(engagementId, NoteVisibility.PUBLIC))
                .thenReturn(new ArrayList<>());
        // Act
        List<NoteDTO> result = noteService.getPublicNotesByEngagementId(engagementId);
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    private List<Note> createPublicNotes(Long engagementId) {
        List<Note> publicNotes = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Note note = new Note();
            note.setId((long) i);
            note.setTitle("Public Note " + i);
            note.setContent("Content " + i);
            note.setVisibility(NoteVisibility.PUBLIC);
            publicNotes.add(note);
        }
        return publicNotes;
    }
    private NoteDTO createNoteDTO(String title, String content, NoteVisibility visibility, Long userId) {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle(title);
        noteDTO.setContent(content);
        noteDTO.setVisibility(visibility);
        noteDTO.setUserId(userId);
        return noteDTO;
    }
    private Note createNoteEntity(Long noteId) {
        Note note = new Note();
        note.setId(noteId);
        note.setTitle("Original Title");
        note.setContent("Original Content");
        note.setVisibility(NoteVisibility.PRIVATE);
        note.setCreatedTime(LocalDateTime.now());
        return note;
    }
    @Test
    void testCreateNote() {
        // Create a sample NoteDTO for testing
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Test Note");
        noteDTO.setContent("Test Content");
        noteDTO.setVisibility(NoteVisibility.PUBLIC);
        noteDTO.setUserId(1L);
        noteDTO.setEngagementId(2L);
        noteDTO.setCreatedTime(LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        Engagement engagement = new Engagement();
        engagement.setId(2L);
        // Mock UserRepository and EngagementRepository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(engagementRepository.findById(2L)).thenReturn(Optional.of(engagement));
        // Create a sample Note entity
        Note savedNote = new Note();
        savedNote.setId(1L);
        savedNote.setTitle("Test Note");
        savedNote.setContent("Test Content");
        savedNote.setVisibility(NoteVisibility.PUBLIC);
        savedNote.setUser(user);
        savedNote.setEngagement(engagement);
        savedNote.setCreatedTime(LocalDateTime.now());
        // Mock the NoteRepository to return the sample Note entity
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);
        // Call the createNote method
        NoteDTO createdNoteDTO = noteService.createNote(noteDTO);
        // Assertions
        assertNotNull(createdNoteDTO);
        assertEquals("Test Note", createdNoteDTO.getTitle());
        assertEquals("Test Content", createdNoteDTO.getContent());
        assertEquals(NoteVisibility.PUBLIC, createdNoteDTO.getVisibility());
        assertEquals(1L, createdNoteDTO.getUserId());
        assertEquals(2L, createdNoteDTO.getEngagementId());
        assertNotNull(createdNoteDTO.getCreatedTime());
        assertNull(createdNoteDTO.getUpdatedTime());
    }
    @Test
    void testUpdateNote() {
        // Create a sample NoteDTO for testing
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Updated Note");
        noteDTO.setContent("Updated Content");
        noteDTO.setVisibility(NoteVisibility.PRIVATE);
        noteDTO.setUserId(1L);
        // Create a sample Note entity
        Note existingNote = new Note();
        existingNote.setId(1L);
        existingNote.setTitle("Original Note");
        existingNote.setContent("Original Content");
        existingNote.setVisibility(NoteVisibility.PUBLIC);
        existingNote.setUser(null);
        existingNote.setUpdatedTime(null);
        // Mock UserRepository to return the user when findById is called
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Mock NoteRepository to return the existingNote when findById is called and return the updatedNote when save is called
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));
        // Call the updateNote method
        NoteDTO updatedNoteDTO = noteService.updateNote(1L, noteDTO);
        // Assertions
        assertNotNull(updatedNoteDTO);
        assertEquals("Updated Note", updatedNoteDTO.getTitle());
        assertEquals("Updated Content", updatedNoteDTO.getContent());
        assertEquals(NoteVisibility.PRIVATE, updatedNoteDTO.getVisibility());
        assertEquals(1L, updatedNoteDTO.getUserId());
        assertNotNull(updatedNoteDTO.getUpdatedTime());
    }
//    @Test
//    void testDeleteNote() {
//        Note existingNote = new Note();
//        existingNote.setId(1L);
//        // Mock NoteRepository to return the existingNote when findById is called
//        when(noteRepository.findById(1L)).thenReturn(java.util.Optional.of(existingNote));
//        // Call the deleteNote method
//        noteService.deleteNote(1L);
//        verify(noteRepository, times(1)).delete(existingNote);
//    }
    @Test
    void testCreateNoteWithoutEngagement() {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Test Note");
        noteDTO.setContent("Test Content");
        noteDTO.setVisibility(NoteVisibility.PUBLIC);
        noteDTO.setUserId(1L);
        noteDTO.setCreatedTime(LocalDateTime.now());
        User user = new User();
        user.setId(1L);
        // Mock UserRepository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Create a sample Note entity
        Note savedNote = new Note();
        savedNote.setId(1L);
        savedNote.setTitle("Test Note");
        savedNote.setContent("Test Content");
        savedNote.setVisibility(NoteVisibility.PUBLIC);
        savedNote.setUser(user);
        savedNote.setCreatedTime(LocalDateTime.now());
        // Mock the NoteRepository to return the sample Note entity
        when(noteRepository.save(any(Note.class))).thenReturn(savedNote);
        NoteDTO createdNoteDTO = noteService.createNote(noteDTO);
        // Assertions
        assertNotNull(createdNoteDTO);
        assertEquals("Test Note", createdNoteDTO.getTitle());
        assertEquals("Test Content", createdNoteDTO.getContent());
        assertEquals(NoteVisibility.PUBLIC, createdNoteDTO.getVisibility());
        assertEquals(1L, createdNoteDTO.getUserId());
        assertNull(createdNoteDTO.getEngagementId());
        assertNotNull(createdNoteDTO.getCreatedTime());
        assertNull(createdNoteDTO.getUpdatedTime());
    }
    @Test
    void testUpdateNoteWithoutUserId() {
        NoteDTO noteDTO = new NoteDTO();
        noteDTO.setTitle("Updated Note");
        noteDTO.setContent("Updated Content");
        noteDTO.setVisibility(NoteVisibility.PRIVATE);
        noteDTO.setUserId(null);
        // Create a sample Note entity
        Note existingNote = new Note();
        existingNote.setId(1L);
        existingNote.setTitle("Original Note");
        existingNote.setContent("Original Content");
        existingNote.setVisibility(NoteVisibility.PUBLIC);
        existingNote.setUpdatedTime(null);
        // Mock NoteRepository to return the existingNote when findById is called and return the updatedNote when save is called
        when(noteRepository.findById(1L)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> invocation.getArgument(0));
        NoteDTO updatedNoteDTO = noteService.updateNote(1L, noteDTO);
        // Assertions
        assertNotNull(updatedNoteDTO);
        assertEquals("Updated Note", updatedNoteDTO.getTitle());
        assertEquals("Updated Content", updatedNoteDTO.getContent());
        assertEquals(NoteVisibility.PRIVATE, updatedNoteDTO.getVisibility());
        assertNull(updatedNoteDTO.getUserId());
        assertNotNull(updatedNoteDTO.getUpdatedTime());
    }
}
