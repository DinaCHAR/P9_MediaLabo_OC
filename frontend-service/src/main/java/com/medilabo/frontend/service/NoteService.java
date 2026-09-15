package com.medilabo.frontend.service;

import com.medilabo.frontend.dto.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


@Service
public class NoteService {

    private final RestTemplate restTemplate;
    
    @Value("${gateway.url:http://gateway-service:8080}")
    private String gatewayUrl;

    public NoteService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Note> getNotesByPatientId(Long patientId) {
        try {
            String url = gatewayUrl + "/api/notes/patient/" + patientId;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getBody() != null) {
                return response.getBody().stream()
                    .map(this::mapToNote)
                    .toList();
            }
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des notes: " + e.getMessage(), e);
        }
    }


    public Note getNoteById(String id) {
        try {
            String url = gatewayUrl + "/api/notes/" + id;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getBody() != null) {
                return mapToNote(response.getBody());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la note: " + e.getMessage(), e);
        }
    }


    public Note createNote(Note note) {
        try {
            String url = gatewayUrl + "/api/notes";
            

            Map<String, Object> createRequest = Map.of(
                "patientId", note.getPatientId(),
                "content", note.getNoteContent()
            );
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(createRequest),
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getBody() != null) {
                return mapToNote(response.getBody());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la note: " + e.getMessage(), e);
        }
    }


    public Note updateNote(String id, Note note) {
        try {
            String url = gatewayUrl + "/api/notes/" + id;
            restTemplate.put(url, note);
            return getNoteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la note: " + e.getMessage(), e);
        }
    }


    public void deleteNote(String id) {
        try {
            String url = gatewayUrl + "/api/notes/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de la note: " + e.getMessage(), e);
        }
    }


    public void deleteNotesByPatientId(Long patientId) {
        try {
            String url = gatewayUrl + "/api/notes/patient/" + patientId;
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression des notes du patient: " + e.getMessage(), e);
        }
    }


    public List<Note> searchNotes(String searchTerm) {
        try {
            String url = gatewayUrl + "/api/notes/search?q=" + searchTerm;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getBody() != null) {
                return response.getBody().stream()
                    .map(this::mapToNote)
                    .toList();
            }
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de notes: " + e.getMessage(), e);
        }
    }


    private Note mapToNote(Map<String, Object> data) {
        Note note = new Note();
        note.setId((String) data.get("id"));
        note.setPatientId(data.get("patientId") != null ? ((Number) data.get("patientId")).longValue() : null);

        note.setNoteContent((String) data.get("content"));

        note.setPractitionerName("Dr. Médecin");
        

        if (data.get("createdAt") != null) {
            String dateStr = data.get("createdAt").toString();
            try {

                if (dateStr.contains("T")) {
                    note.setCreatedDate(LocalDateTime.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                } else {

                    note.setCreatedDate(LocalDateTime.parse(dateStr));
                }
            } catch (Exception e) {

                System.err.println("Erreur lors du parsing de la date: " + dateStr);
                note.setCreatedDate(LocalDateTime.now());
            }
        }
        

        @SuppressWarnings("unchecked")
        List<String> keywords = (List<String>) data.get("keywords");
        note.setKeywords(keywords);
        
        return note;
    }
}