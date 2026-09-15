package com.medilabo.note.service;

import com.medilabo.note.dto.CreateNoteRequest;
import com.medilabo.note.dto.NoteDto;
import com.medilabo.note.dto.UpdateNoteRequest;
import com.medilabo.note.entity.Note;
import com.medilabo.note.exception.NoteNotFoundException;
import com.medilabo.note.mapper.NoteMapper;
import com.medilabo.note.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    public NoteDto createNote(CreateNoteRequest createRequest) {
        Note note = noteMapper.toEntity(createRequest);
        Note savedNote = noteRepository.save(note);
        return noteMapper.toDto(savedNote);
    }

    public NoteDto getNoteById(String id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        return noteMapper.toDto(note);
    }

    public List<NoteDto> getNotesByPatientId(Long patientId) {
        List<Note> notes = noteRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
        return noteMapper.toDtoList(notes);
    }

    public List<NoteDto> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return noteMapper.toDtoList(notes);
    }

    public NoteDto updateNote(String id, UpdateNoteRequest updateRequest) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(id));
        
        noteMapper.updateEntity(note, updateRequest);
        Note updatedNote = noteRepository.save(note);
        return noteMapper.toDto(updatedNote);
    }

    public void deleteNote(String id) {
        if (!noteRepository.existsById(id)) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }

    @Transactional
    public void deleteNotesByPatientId(Long patientId) {
        noteRepository.deleteByPatientId(patientId);
    }
}