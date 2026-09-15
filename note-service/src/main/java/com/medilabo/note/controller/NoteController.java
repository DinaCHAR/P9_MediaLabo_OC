package com.medilabo.note.controller;

import com.medilabo.note.dto.CreateNoteRequest;
import com.medilabo.note.dto.NoteDto;
import com.medilabo.note.dto.UpdateNoteRequest;
import com.medilabo.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;


    @PostMapping
    public ResponseEntity<NoteDto> createNote(@Valid @RequestBody CreateNoteRequest createRequest) {
        NoteDto createdNote = noteService.createNote(createRequest);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable String id) {
        NoteDto note = noteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }


    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NoteDto>> getNotesByPatientId(@PathVariable Long patientId) {
        List<NoteDto> notes = noteService.getNotesByPatientId(patientId);
        return ResponseEntity.ok(notes);
    }


    @GetMapping
    public ResponseEntity<List<NoteDto>> getAllNotes() {
        List<NoteDto> notes = noteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }


    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable String id, 
                                              @Valid @RequestBody UpdateNoteRequest updateRequest) {
        NoteDto updatedNote = noteService.updateNote(id, updateRequest);
        return ResponseEntity.ok(updatedNote);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/patient/{patientId}")
    public ResponseEntity<Void> deleteNotesByPatientId(@PathVariable Long patientId) {
        noteService.deleteNotesByPatientId(patientId);
        return ResponseEntity.noContent().build();
    }
}