package com.medilabo.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskAssessmentDto {
    private Long patientId;
    private String patientName;
    private int age;
    private String gender;
    private String riskLevel;
    private String riskDescription;
    private java.util.List<String> triggersFound;

    public RiskAssessmentDto() {}


    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public java.util.List<String> getTriggersFound() {
        return triggersFound;
    }

    public void setTriggersFound(java.util.List<String> triggersFound) {
        this.triggersFound = triggersFound;
    }


    public enum RiskLevel {
        NONE("Aucun risque"),
        BORDERLINE("Risque limite"),
        IN_DANGER("En danger"),
        EARLY_ONSET("Apparition précoce");

        private final String description;

        RiskLevel(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}