package com.medilabo.frontend.service;

import com.medilabo.frontend.dto.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Service
public class PatientService {

    private final RestTemplate restTemplate;
    private final NoteService noteService;
    
    @Value("${gateway.url:http://gateway-service:8080}")
    private String gatewayUrl;

    public PatientService(RestTemplate restTemplate, NoteService noteService) {
        this.restTemplate = restTemplate;
        this.noteService = noteService;
    }


    public SimplePageImpl<Patient> getAllPatients(int page, int size) {
        try {
            String url = gatewayUrl + "/api/patients?page=" + page + "&size=" + size;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            Map<String, Object> pageData = response.getBody();
            if (pageData != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> content = (List<Map<String, Object>>) pageData.get("content");
                List<Patient> patients = content.stream()
                    .map(this::mapToPatient)
                    .toList();
                
                int totalElements = ((Number) pageData.get("totalElements")).intValue();

                return new SimplePageImpl<>(patients, page, size, totalElements);
            }
            return new SimplePageImpl<>(List.of(), page, size, 0);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des patients: " + e.getMessage(), e);
        }
    }


    public Patient getPatientById(Long id) {
        try {
            String url = gatewayUrl + "/api/patients/" + id;
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getBody() != null) {
                return mapToPatient(response.getBody());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération du patient: " + e.getMessage(), e);
        }
    }


    public List<Patient> searchPatientsByName(String name) {
        try {
            String url = gatewayUrl + "/api/patients/search?name=" + name;
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            if (response.getBody() != null) {
                return response.getBody().stream()
                    .map(this::mapToPatient)
                    .toList();
            }
            return List.of();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche de patients: " + e.getMessage(), e);
        }
    }


    public Patient createPatient(Patient patient) {
        try {
            String url = gatewayUrl + "/api/patients";
            

            Map<String, Object> patientData = Map.of(
                "firstName", patient.getFirstName(),
                "lastName", patient.getLastName(),
                "birthDate", patient.getDateOfBirth(),
                "gender", patient.getGender(),
                "address", patient.getAddress() != null ? patient.getAddress() : "",
                "phoneNumber", patient.getPhoneNumber() != null ? patient.getPhoneNumber() : ""
            );
            
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(patientData),
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            
            if (response.getBody() != null) {
                return mapToPatient(response.getBody());
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création du patient: " + e.getMessage(), e);
        }
    }


    public Patient updatePatient(Long id, Patient patient) {
        try {
            String url = gatewayUrl + "/api/patients/" + id;
            

            Map<String, Object> patientData = Map.of(
                "firstName", patient.getFirstName(),
                "lastName", patient.getLastName(),
                "birthDate", patient.getDateOfBirth(),
                "gender", patient.getGender(),
                "address", patient.getAddress() != null ? patient.getAddress() : "",
                "phoneNumber", patient.getPhoneNumber() != null ? patient.getPhoneNumber() : ""
            );
            
            restTemplate.put(url, patientData);
            return getPatientById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la mise à jour du patient: " + e.getMessage(), e);
        }
    }


    public void deletePatient(Long id) {
        try {

            noteService.deleteNotesByPatientId(id);
            

            String url = gatewayUrl + "/api/patients/" + id;
            restTemplate.delete(url);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du patient: " + e.getMessage(), e);
        }
    }


    private Patient mapToPatient(Map<String, Object> data) {
        Patient patient = new Patient();
        patient.setId(data.get("id") != null ? ((Number) data.get("id")).longValue() : null);
        patient.setFirstName((String) data.get("firstName"));
        patient.setLastName((String) data.get("lastName"));
        

        Object birthDateObj = data.get("birthDate");
        if (birthDateObj == null) {

            birthDateObj = data.get("dateOfBirth");
        }
        
        if (birthDateObj != null) {
            String dateStr = birthDateObj.toString();
            try {
                patient.setDateOfBirth(java.time.LocalDate.parse(dateStr));
            } catch (Exception e) {

                System.err.println("Erreur lors du parsing de la date: " + dateStr);
            }
        }
        
        patient.setGender((String) data.get("gender"));
        patient.setAddress((String) data.get("address"));
        patient.setPhoneNumber((String) data.get("phoneNumber"));
        
        return patient;
    }
}