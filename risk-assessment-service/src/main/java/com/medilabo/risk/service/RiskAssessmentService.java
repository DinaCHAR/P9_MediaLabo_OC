package com.medilabo.risk.service;

import com.medilabo.risk.client.NoteServiceClient;
import com.medilabo.risk.client.PatientServiceClient;
import com.medilabo.risk.dto.NoteDto;
import com.medilabo.risk.dto.PatientDto;
import com.medilabo.risk.dto.RiskAssessmentDto;
import com.medilabo.risk.model.RiskLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RiskAssessmentService {

    @Autowired
    private PatientServiceClient patientServiceClient;

    @Autowired
    private NoteServiceClient noteServiceClient;


    private static final List<String> RISK_TRIGGERS = Arrays.asList(
            "hémoglobine a1c", "microalbumine", "taille", "poids", "fumeur", "anormal",
            "cholestérol", "vertige", "rechute", "réaction", "anticorps"
    );


    public RiskAssessmentDto assessRisk(Long patientId) {

        PatientDto patient = patientServiceClient.getPatientById(patientId);
        

        List<NoteDto> notes = noteServiceClient.getNotesByPatientId(patientId);
        

        int age = calculateAge(patient.getBirthDate());
        

        List<String> triggersFound = findTriggersInNotes(notes);
        

        RiskLevel riskLevel = evaluateRiskLevel(age, patient.getGender(), triggersFound.size());
        
        return new RiskAssessmentDto(
                patient.getId(),
                patient.getFullName(),
                age,
                patient.getGender(),
                riskLevel,
                triggersFound
        );
    }


    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }


    private List<String> findTriggersInNotes(List<NoteDto> notes) {
        return notes.stream()
                .flatMap(note -> RISK_TRIGGERS.stream()
                        .filter(trigger -> note.getContent().toLowerCase().contains(trigger.toLowerCase()))
                )
                .distinct()
                .collect(Collectors.toList());
    }


    private RiskLevel evaluateRiskLevel(int age, String gender, int triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(gender);
        
        if (age < 30) {

            if (isMale) {
                if (triggerCount >= 5) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 3) return RiskLevel.IN_DANGER;
            } else {
                if (triggerCount >= 7) return RiskLevel.EARLY_ONSET;
                if (triggerCount >= 4) return RiskLevel.IN_DANGER;
            }
        } else {

            if (triggerCount >= 8) return RiskLevel.EARLY_ONSET;
            if (triggerCount >= 6) return RiskLevel.IN_DANGER;
            if (triggerCount >= 2) return RiskLevel.BORDERLINE;
        }
        
        return RiskLevel.NONE;
    }
}