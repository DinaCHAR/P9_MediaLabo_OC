package com.medilabo.risk.dto;

import com.medilabo.risk.model.RiskLevel;

import java.time.LocalDateTime;
import java.util.List;


public class RiskAssessmentDto {

    private Long patientId;
    private String patientName;
    private int age;
    private String gender;
    private RiskLevel riskLevel;
    private String riskDescription;
    private List<String> triggersFound;
    private int triggerCount;
    private LocalDateTime assessmentDate;

    public RiskAssessmentDto() {
        this.assessmentDate = LocalDateTime.now();
    }

    public RiskAssessmentDto(Long patientId, String patientName, int age, String gender, 
                            RiskLevel riskLevel, List<String> triggersFound) {
        this();
        this.patientId = patientId;
        this.patientName = patientName;
        this.age = age;
        this.gender = gender;
        this.riskLevel = riskLevel;
        this.riskDescription = riskLevel.getDescription();
        this.triggersFound = triggersFound;
        this.triggerCount = triggersFound != null ? triggersFound.size() : 0;
    }

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

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
        this.riskDescription = riskLevel != null ? riskLevel.getDescription() : null;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public List<String> getTriggersFound() {
        return triggersFound;
    }

    public void setTriggersFound(List<String> triggersFound) {
        this.triggersFound = triggersFound;
        this.triggerCount = triggersFound != null ? triggersFound.size() : 0;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
    }

    public LocalDateTime getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(LocalDateTime assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
}