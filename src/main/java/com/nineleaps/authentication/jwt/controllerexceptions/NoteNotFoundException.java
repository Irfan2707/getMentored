package com.nineleaps.authentication.jwt.controllerexceptions;

public class NoteNotFoundException extends RuntimeException {
    public NoteNotFoundException(Long noteId) {
        super("Note not found with ID: " + noteId);
    }
}

