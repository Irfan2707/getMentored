
package com.nineleaps.authentication.jwt.service.implementation;

import com.nineleaps.authentication.jwt.controllerexceptions.NoteNotFoundException;
import com.nineleaps.authentication.jwt.dto.NoteDTO;
import com.nineleaps.authentication.jwt.entity.Engagement;
import com.nineleaps.authentication.jwt.entity.Note;
import com.nineleaps.authentication.jwt.entity.User;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import com.nineleaps.authentication.jwt.repository.EngagementRepository;
import com.nineleaps.authentication.jwt.repository.NoteRepository;
import com.nineleaps.authentication.jwt.repository.UserRepository;
import com.nineleaps.authentication.jwt.service.interfaces.INoteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService implements INoteService {

    private final NoteRepository noteRepository;

    private final EngagementRepository engagementRepository;

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);

    @Override
    public NoteDTO createNote(NoteDTO privateNoteDTO) {
        logger.info("Creating a new note");

        Note note = new Note();
        note.setTitle(privateNoteDTO.getTitle());
        note.setContent(privateNoteDTO.getContent());
        note.setVisibility(privateNoteDTO.getVisibility());
        note.setCreatedTime(LocalDateTime.now()); // Set the current timestamp as the creation time

        Long userId = privateNoteDTO.getUserId();
        User user = userRepository.findById(userId).orElseThrow();

        note.setUser(user);

        if (privateNoteDTO.getEngagementId() != null) {
            Engagement engagement = engagementRepository.findById(privateNoteDTO.getEngagementId()).orElseThrow();
            note.setEngagement(engagement);
        }

        Note savedNote = noteRepository.save(note);

        logger.info("Created a new note with ID: {}", savedNote.getId());

        return mapNoteToDTO(savedNote);
    }


    @Override
    public NoteDTO updateNote(Long noteId, NoteDTO noteDTO) {
        logger.info("Updating note with ID: {}", noteId);

        Note note = noteRepository.findById(noteId).orElseThrow();

        note.setContent(noteDTO.getContent());
        note.setVisibility(noteDTO.getVisibility());
        note.setTitle(noteDTO.getTitle());
        note.setUpdatedTime(LocalDateTime.now()); // Set the current timestamp as the update time

        // Set the User only if the userId is not null
        if (noteDTO.getUserId() != null) {
            User user = userRepository.findById(noteDTO.getUserId()).orElseThrow();
            note.setUser(user);
        }

        Note updatedNote = noteRepository.save(note);

        logger.info("Updated note with ID: {}", updatedNote.getId());

        return mapNoteToDTO(updatedNote);
    }

    @Override
    public void deleteNote(Long noteId) {
        logger.info("Deleting note with ID: {}", noteId);

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException(noteId));


        note.setDeleted(true);
        note.setUpdatedTime(LocalDateTime.now());

        noteRepository.save(note); // Update the record with the deleted flag and updated time

        logger.info("Deleted note with ID: {}", noteId);
    }


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


    @Override
    public List<NoteDTO> getPublicNotesByEngagementId(Long engagementId) {
        logger.info("Fetching public notes by engagement ID: {}", engagementId);

        List<Note> notes = noteRepository.findByEngagementIdAndVisibility(engagementId, NoteVisibility.PUBLIC);

        logger.info("Fetched {} public notes by engagement ID: {}", notes.size(), engagementId);

        return notes.stream().map(this::mapNoteToDTO).collect(Collectors.toList());
    }

    @Override
    public List<NoteDTO> getPrivateNotesByUserIdAndEngagementId(Long userId, Long engagementId) {
        logger.info("Fetching private notes by user ID: {} and engagement ID: {}", userId, engagementId);

        List<Note> notes = noteRepository.findByUserIdAndEngagementIdAndVisibility(userId, engagementId, NoteVisibility.PRIVATE);

        logger.info("Fetched {} private notes by user ID: {} and engagement ID: {}", notes.size(), userId, engagementId);

        return notes.stream().map(this::mapNoteToDTO).collect(Collectors.toList());
    }


}