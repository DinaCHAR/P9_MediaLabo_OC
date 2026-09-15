package com.medilabo.note.repository;

import com.medilabo.note.entity.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteRepository extends MongoRepository<Note, String> {


    List<Note> findByPatientId(Long patientId);


    List<Note> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    void deleteByPatientId(Long patientId);
}