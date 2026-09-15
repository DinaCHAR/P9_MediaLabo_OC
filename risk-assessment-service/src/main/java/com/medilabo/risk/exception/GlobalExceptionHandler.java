package com.medilabo.risk.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        
        if (ex.status() == 404) {
            errorResponse.put("error", "Ressource non trouvée");
            errorResponse.put("message", "Le patient ou les notes demandés n'ont pas été trouvés");
        } else {
            errorResponse.put("error", "Erreur de communication");
            errorResponse.put("message", "Impossible de communiquer avec les services externes");
        }
        
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", ex.status());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.status()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Erreur interne du serveur");
        errorResponse.put("message", "Une erreur inattendue s'est produite lors de l'évaluation des risques");
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}