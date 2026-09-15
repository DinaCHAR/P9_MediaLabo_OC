package com.medilabo.patient.controller;

import com.medilabo.patient.dto.CreatePatientRequest;
import com.medilabo.patient.dto.PatientDto;
import com.medilabo.patient.dto.UpdatePatientRequest;
import com.medilabo.patient.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;


    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody CreatePatientRequest request) {
        PatientDto createdPatient = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        PatientDto patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }


    @GetMapping
    public ResponseEntity<Page<PatientDto>> getAllPatients(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size) {
        Page<PatientDto> patients = patientService.getAllPatients(page, size);
        return ResponseEntity.ok(patients);
    }


    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatientsByName(@RequestParam(name = "name") String name) {
        List<PatientDto> patients = patientService.searchPatientsByName(name);
        return ResponseEntity.ok(patients);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {
        PatientDto updatedPatient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(updatedPatient);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}