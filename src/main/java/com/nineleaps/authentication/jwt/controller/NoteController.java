package com.nineleaps.authentication.jwt.controller;

import com.nineleaps.authentication.jwt.dto.NoteDTO;
import com.nineleaps.authentication.jwt.response.ResponseHandler;
import com.nineleaps.authentication.jwt.service.implementation.NoteService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
@RequiredArgsConstructor
public class NoteController {


    private final NoteService noteService;
    private final Logger logger = LoggerFactory.getLogger(NoteController.class);


    /**
     * Endpoint to create notes for both Mentor and Mentee.
     *
     * @param privateNoteDTO The NoteDTO containing the details of the note to be created.
     * @return A response entity indicating the result of the note creation process.
     */
    @PostMapping("/createNotesForMenteeAndMentor")
    @ApiOperation("Write Notes For Both Mentor and Mentee")
    public ResponseEntity<Object> createNote(@RequestBody NoteDTO privateNoteDTO) {
        NoteDTO noteResponseDTO = noteService.createNote(privateNoteDTO);

        if (noteResponseDTO == null) {
            logger.error("Failed to create note.");
            return ResponseHandler.error("Failed to create note.", HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            logger.info("Note created successfully.");
            return ResponseHandler.success("Note created successfully.", HttpStatus.CREATED, noteResponseDTO);

        }
    }


    /**
     * Endpoint to update notes for both Mentor and Mentee by Note Id.
     *
     * @param noteId   The ID of the note to be updated.
     * @param noteDTO  The NoteDTO containing the updated details.
     * @return A response entity indicating the result of the note update process.
     */
    @PutMapping("/updatingNotes")
    @ApiOperation("Updating the Notes For Both Mentor and Mentee by Note Id")
    public ResponseEntity<Object> updateNote(
            @RequestParam Long noteId,
            @RequestBody NoteDTO noteDTO
    ) {
        NoteDTO updatedNote = noteService.updateNote(noteId, noteDTO);

        if (updatedNote != null) {
            logger.info("Note updated successfully.");
            return ResponseHandler.success("Note updated successfully.", HttpStatus.OK, updatedNote);
        } else {
            logger.error("Failed to update note.");
            return ResponseHandler.error("Failed to update note.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint to delete notes by Note Id.
     *
     * @param noteId The ID of the note to be deleted.
     * @return A response entity indicating the result of the note deletion process.
     */
    @DeleteMapping("/delete")
    @ApiOperation("Deleting the Notes by Note Id")
    public ResponseEntity<Object> deleteNote(@RequestParam Long noteId) {
        noteService.deleteNote(noteId);
        logger.info("Note deleted successfully.");
        return ResponseHandler.success("Note deleted successfully.", HttpStatus.OK, null);
    }

    /**
     * Endpoint to get public notes of both Mentor and Mentee by Engagement Id.
     *
     * @param engagementId The ID of the engagement to retrieve public notes.
     * @return A response entity containing the list of public notes.
     */
    @GetMapping("/gettingPublicNotes")
    @ApiOperation("Get Public notes of both Mentor and Mentee by Engagement Id")
    public ResponseEntity<Object> getPublicNotesByEngagementId(@RequestParam Long engagementId) {
        List<NoteDTO> publicNotes = noteService.getPublicNotesByEngagementId(engagementId);

        if (publicNotes == null) {
            logger.error("No public notes found for the given Engagement Id.");
            return ResponseHandler.error("No public notes found for the given Engagement Id.", HttpStatus.NOT_FOUND);
        } else {
            logger.info("Public notes retrieved successfully.");
            return ResponseHandler.success("Public notes retrieved successfully.", HttpStatus.OK, publicNotes);

        }
    }

    /**
     * Endpoint to get private notes of both Mentor and Mentee by User Id and Engagement Id.
     *
     * @param userId       The ID of the user to retrieve private notes.
     * @param engagementId The ID of the engagement to retrieve private notes.
     * @return A response entity containing the list of private notes.
     */
    @GetMapping("/gettingPrivateNotes")
    @ApiOperation("Get Private notes of both Mentor and Mentee by User Id and Engagement Id")
    public ResponseEntity<Object> getPrivateNotesByUserIdAndEngagementId(
            @RequestParam Long userId,
            @RequestParam Long engagementId
    ) {
        List<NoteDTO> privateNotes = noteService.getPrivateNotesByUserIdAndEngagementId(userId, engagementId);

        if (privateNotes == null) {
            logger.error("No private notes found for the given User Id and Engagement Id.");
            return ResponseHandler.error("No private notes found for the given User Id and Engagement Id.", HttpStatus.NOT_FOUND);
        } else {

            logger.info("Private notes retrieved successfully.");
            return ResponseHandler.success("Private notes retrieved successfully.", HttpStatus.OK, privateNotes);

        }
    }

}
