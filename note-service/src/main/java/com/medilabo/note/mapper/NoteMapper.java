package com.medilabo.note.mapper;

import com.medilabo.note.dto.CreateNoteRequest;
import com.medilabo.note.dto.NoteDto;
import com.medilabo.note.dto.UpdateNoteRequest;
import com.medilabo.note.entity.Note;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    public NoteDto toDto(Note note) {
        if (note == null) {
            return null;
        }
        return new NoteDto(
                note.getId(),
                note.getPatientId(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }

    public List<NoteDto> toDtoList(List<Note> notes) {
        return notes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Note toEntity(CreateNoteRequest request) {
        if (request == null) {
            return null;
        }
        return new Note(request.getPatientId(), request.getContent());
    }

    public void updateEntity(Note note, UpdateNoteRequest request) {
        if (note != null && request != null) {
            note.setContent(request.getContent());
        }
    }
}