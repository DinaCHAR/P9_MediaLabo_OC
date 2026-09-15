package com.medilabo.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/fallback")
public class FallbackController {


    @GetMapping("/patient")
    public Mono<ResponseEntity<Map<String, Object>>> patientFallback() {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Service Patient temporairement indisponible");
            response.put("message", "Veuillez réessayer dans quelques instants");
            response.put("service", "patient-service");
            response.put("timestamp", System.currentTimeMillis());
            response.put("status", "SERVICE_UNAVAILABLE");
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        });
    }


    @GetMapping("/note")
    public Mono<ResponseEntity<Map<String, Object>>> noteFallback() {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Service Note temporairement indisponible");
            response.put("message", "Les notes médicales ne peuvent pas être consultées actuellement");
            response.put("service", "note-service");
            response.put("timestamp", System.currentTimeMillis());
            response.put("status", "SERVICE_UNAVAILABLE");
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        });
    }


    @GetMapping("/risk")
    public Mono<ResponseEntity<Map<String, Object>>> riskFallback() {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Service d'évaluation des risques temporairement indisponible");
            response.put("message", "L'évaluation du risque diabétique ne peut pas être effectuée actuellement");
            response.put("service", "risk-assessment-service");
            response.put("timestamp", System.currentTimeMillis());
            response.put("status", "SERVICE_UNAVAILABLE");
            response.put("fallbackData", createRiskFallbackData());
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        });
    }


    @GetMapping("/default")
    public Mono<ResponseEntity<Map<String, Object>>> defaultFallback() {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Service temporairement indisponible");
            response.put("message", "Le service demandé rencontre des difficultés techniques");
            response.put("service", "unknown-service");
            response.put("timestamp", System.currentTimeMillis());
            response.put("status", "SERVICE_UNAVAILABLE");
            response.put("recommendation", "Veuillez contacter l'administrateur si le problème persiste");
            
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
        });
    }


    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> health() {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("service", "gateway-service");
            response.put("timestamp", System.currentTimeMillis());
            response.put("version", "1.0.0");
            response.put("message", "Gateway fonctionne correctement");
            
            return ResponseEntity.ok(response);
        });
    }


    private Map<String, Object> createRiskFallbackData() {
        Map<String, Object> fallbackData = new HashMap<>();
        fallbackData.put("riskLevel", "UNKNOWN");
        fallbackData.put("message", "Impossible d'évaluer le risque actuellement");
        fallbackData.put("recommendation", "Consultez un professionnel de santé");
        fallbackData.put("lastUpdate", null);
        
        return fallbackData;
    }
}