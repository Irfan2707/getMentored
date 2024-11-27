package com.nineleaps.authentication.jwt.controllerExceptionTesting;

import com.nineleaps.authentication.jwt.controllerexceptions.NoteNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NoteNotFoundExceptionTest {

    @Test
    void testNoteNotFoundExceptionMessage() {


        long expectedNoteId = 123;

        // Call the method here
        Throwable exception = assertThrows(NoteNotFoundException.class, () -> {

            throw new NoteNotFoundException(expectedNoteId);
        });

        assertEquals("Note not found with ID: " + expectedNoteId, exception.getMessage());
    }
}
