package com.medilabo.risk.controller;

import com.medilabo.risk.dto.RiskAssessmentDto;
import com.medilabo.risk.service.RiskAssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/risk")
public class RiskAssessmentController {

    @Autowired
    private RiskAssessmentService riskAssessmentService;


    @GetMapping("/assess/{patientId}")
    public ResponseEntity<RiskAssessmentDto> assessRisk(@PathVariable Long patientId) {
        RiskAssessmentDto assessment = riskAssessmentService.assessRisk(patientId);
        return ResponseEntity.ok(assessment);
    }
    
    @PostMapping("/assess/{patientId}")
    public ResponseEntity<RiskAssessmentDto> generateRiskAssessment(@PathVariable Long patientId) {

        RiskAssessmentDto assessment = riskAssessmentService.assessRisk(patientId);
        return ResponseEntity.ok(assessment);
    }


    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Risk Assessment Service is running");
    }
}