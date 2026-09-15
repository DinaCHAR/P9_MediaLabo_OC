package com.medilabo.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Configuration de l'application
 */
@Configuration
public class AppConfig {

    /**
     * Bean RestTemplate pour les appels HTTP vers les services backend
     * Configuré avec l'intercepteur JWT pour l'authentification automatique
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new JwtRequestInterceptor()));
        return restTemplate;
    }
}