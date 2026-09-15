package com.medilabo.patient.service;

import com.medilabo.patient.dto.CreatePatientRequest;
import com.medilabo.patient.dto.PatientDto;
import com.medilabo.patient.dto.UpdatePatientRequest;
import com.medilabo.patient.exception.PatientNotFoundException;
import com.medilabo.patient.mapper.PatientMapper;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;


    public PatientDto createPatient(CreatePatientRequest createRequest) {
        Patient patient = patientMapper.toEntity(createRequest);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }


    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient non trouvé avec l'ID: " + id));
        return patientMapper.toDto(patient);
    }


    @Transactional(readOnly = true)
    public Page<PatientDto> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientsPage = patientRepository.findAll(pageable);
        return patientsPage.map(patientMapper::toDto);
    }


    public PatientDto updatePatient(Long id, UpdatePatientRequest updateRequest) {
        Patient existingPatient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException("Patient non trouvé avec l'ID: " + id));
        
        patientMapper.updateEntityFromRequest(updateRequest, existingPatient);
        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }


    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient non trouvé avec l'ID: " + id);
        }
        patientRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<PatientDto> searchPatientsByName(String name) {
        List<Patient> patients = patientRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return patients.stream()
                .map(patientMapper::toDto)
                .toList();
    }
}