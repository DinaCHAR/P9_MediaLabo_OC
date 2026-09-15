package com.medilabo.patient.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.medilabo.patient.model.Patient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {

    private String firstName;
    private String lastName;
    
    @Past(message = "La date de naissance doit être dans le passé")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    
    private Patient.Gender gender;
    private String address;
    private String phoneNumber;
    
    @Email(message = "L'email doit être valide")
    private String email;

}