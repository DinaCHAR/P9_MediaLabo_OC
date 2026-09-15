package com.medilabo.note.exception;


public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String id) {
        super("Note non trouvée avec l'ID : " + id);
    }

    public NoteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}