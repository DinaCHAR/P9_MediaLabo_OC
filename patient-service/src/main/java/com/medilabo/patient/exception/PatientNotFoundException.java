package com.medilabo.patient.exception;


public class PatientNotFoundException extends RuntimeException {


    public PatientNotFoundException(String message) {
        super(message);
    }


    public PatientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }


    public PatientNotFoundException(Long patientId) {
        super("Patient non trouvé avec l'ID: " + patientId);
    }
}