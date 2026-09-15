package com.medilabo.patient.exception;


public class DuplicatePatientException extends RuntimeException {


    public DuplicatePatientException(String message) {
        super(message);
    }


    public DuplicatePatientException(String message, Throwable cause) {
        super(message, cause);
    }


    public DuplicatePatientException(String firstName, String lastName, String birthDate) {
        super(String.format("Un patient avec le nom '%s %s' et la date de naissance '%s' existe déjà",
                firstName, lastName, birthDate));
    }
}