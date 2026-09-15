package com.medilabo.gateway.config;

import com.medilabo.gateway.security.JwtAuthenticationFilter;
import com.medilabo.gateway.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges

                        .pathMatchers("/auth/**", "/login", "/actuator/health", "/actuator/gateway/**").permitAll()
                        .pathMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                        

                        .pathMatchers(HttpMethod.GET, "/api/patients/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.PUT, "/api/patients/**").hasAnyRole("ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")
                        
                        .pathMatchers(HttpMethod.GET, "/api/notes/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.POST, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.PUT, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                        .pathMatchers(HttpMethod.DELETE, "/api/notes/**").hasAnyRole("ADMIN", "DOCTOR")
                        
                        .pathMatchers("/api/risk/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        

                        .pathMatchers("/dashboard/**", "/patients/**", "/notes/**").hasAnyRole("USER", "ADMIN", "DOCTOR")
                        

                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        

        configuration.setAllowedOriginPatterns(List.of(
            "http://localhost:*",
            "https://localhost:*",
            "http://medilabo-frontend:*",
            "https://*.medilabo-solutions.com"
        ));
        

        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"
        ));
        

        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-User-Id"
        ));
        

        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization"
        ));
        
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}