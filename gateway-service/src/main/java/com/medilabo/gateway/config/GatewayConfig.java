package com.medilabo.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class GatewayConfig {

    @Value("${services.patient.url:http://patient-service:8081}")
    private String patientServiceUrl;
    
    @Value("${services.note.url:http://note-service:8082}")
    private String noteServiceUrl;
    
    @Value("${services.risk.url:http://risk-assessment-service:8083}")
    private String riskServiceUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
        
                .route("patient-service", r -> r
                        .path("/api/patients/**")
                        .uri(patientServiceUrl)
                )
                
        
                .route("note-service", r -> r
                        .path("/api/notes/**")
                        .uri(noteServiceUrl)
                )
                
        
                .route("risk-assessment-service", r -> r
                        .path("/api/risk/**")
                        .uri(riskServiceUrl)
                )
                
                .build();
    }

}