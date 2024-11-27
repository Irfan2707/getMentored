package com.nineleaps.authentication.jwt.service.interfaces;

import com.nineleaps.authentication.jwt.dto.NoteDTO;

import java.util.List;


public interface INoteService {

    NoteDTO createNote(NoteDTO privateNoteDTO);

    NoteDTO updateNote(Long noteId, NoteDTO noteDTO);

    void deleteNote(Long noteId);


    List<NoteDTO> getPublicNotesByEngagementId(Long engagementId);

    List<NoteDTO> getPrivateNotesByUserIdAndEngagementId(Long userId, Long engagementId);
}
