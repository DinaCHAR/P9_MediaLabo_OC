package com.medilabo.frontend.service;

import com.medilabo.frontend.dto.Patient;
import com.medilabo.frontend.dto.RiskAssessment;
import com.medilabo.frontend.dto.RiskAssessmentDto;
import com.medilabo.frontend.service.SimplePageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class RiskAssessmentService {

    private static final Logger logger = LoggerFactory.getLogger(RiskAssessmentService.class);

    @Value("${gateway.url:http://gateway-service:8080}")
    private String riskAssessmentServiceUrl;

    private final RestTemplate restTemplate;
    private final PatientService patientService;

    public RiskAssessmentService(RestTemplate restTemplate, PatientService patientService) {
        this.restTemplate = restTemplate;
        this.patientService = patientService;
    }


    public RiskAssessment getRiskAssessmentForPatient(Long patientId) {
        try {
            logger.info("Récupération de l'évaluation de risque pour le patient ID: {}", patientId);
            

            Patient patient = patientService.getPatientById(patientId);
            if (patient == null) {
                logger.warn("Patient non trouvé avec l'ID: {}", patientId);
                return null;
            }


            String url = riskAssessmentServiceUrl + "/api/risk/assess/" + patientId;
            logger.debug("Appel du service de risque: {}", url);
            
            ResponseEntity<RiskAssessmentDto> response = restTemplate.getForEntity(url, RiskAssessmentDto.class);
            RiskAssessmentDto riskDto = response.getBody();
            String riskLevel = riskDto != null ? riskDto.getRiskLevel() : "NONE";
            

            int age = calculateAge(patient.getDateOfBirth());
            

            RiskAssessment riskAssessment = new RiskAssessment(
                patientId,
                patient.getFirstName(),
                patient.getLastName(),
                age,
                patient.getGender(),
                riskLevel
            );
            
            logger.info("Évaluation de risque récupérée avec succès pour le patient {}: {}", patientId, riskLevel);
            return riskAssessment;
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de l'évaluation de risque pour le patient {}: {}", patientId, e.getMessage());
            return null;
        }
    }


    public List<RiskAssessment> getAllRiskAssessments() {
        try {
            logger.info("Récupération de toutes les évaluations de risque");
            

            SimplePageImpl<Patient> patientsPage = patientService.getAllPatients(0, 1000);
            List<Patient> patients = patientsPage.getContent();
            List<RiskAssessment> riskAssessments = new ArrayList<>();
            
            for (Patient patient : patients) {
                try {

                    String url = riskAssessmentServiceUrl + "/api/risk/assess/" + patient.getId();
                    ResponseEntity<RiskAssessmentDto> response = restTemplate.getForEntity(url, RiskAssessmentDto.class);
                    RiskAssessmentDto riskDto = response.getBody();
                    String riskLevel = riskDto != null ? riskDto.getRiskLevel() : "NONE";
                    

                    int age = calculateAge(patient.getDateOfBirth());
                    

                    RiskAssessment riskAssessment = new RiskAssessment(
                        patient.getId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        age,
                        patient.getGender(),
                        riskLevel
                    );
                    
                    riskAssessments.add(riskAssessment);
                    logger.debug("Évaluation de risque ajoutée pour le patient {}: {}", patient.getId(), riskLevel);
                    
                } catch (Exception e) {
                    logger.warn("Impossible de récupérer l'évaluation de risque pour le patient {}: {}", patient.getId(), e.getMessage());

                    RiskAssessment defaultAssessment = new RiskAssessment(
                        patient.getId(),
                        patient.getFirstName(),
                        patient.getLastName(),
                        calculateAge(patient.getDateOfBirth()),
                        patient.getGender(),
                        "None"
                    );
                    riskAssessments.add(defaultAssessment);
                }
            }
            
            logger.info("Récupération terminée: {} évaluations de risque", riskAssessments.size());
            return riskAssessments;
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de toutes les évaluations de risque: {}", e.getMessage());
            return new ArrayList<>();
        }
    }


    private int calculateAge(LocalDate birthDate) {
        if (birthDate == null) {
            return 0;
        }
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}