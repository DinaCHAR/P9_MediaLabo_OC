package com.medilabo.gateway.controller;

import com.medilabo.gateway.dto.LoginRequest;
import com.medilabo.gateway.dto.LoginResponse;
import com.medilabo.gateway.dto.RefreshTokenRequest;
import com.medilabo.gateway.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    

    private final Map<String, UserInfo> users;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.users = initializeUsers();
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return Mono.fromCallable(() -> {
            UserInfo user = users.get(loginRequest.getUsername());
            
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse(null, null, "Identifiants invalides", null, null));
            }

            String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRoles(), user.getId());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername(), user.getId());

            LoginResponse response = new LoginResponse(
                    accessToken,
                    refreshToken,
                    "Connexion réussie",
                    user.getUsername(),
                    user.getRoles()
            );

            return ResponseEntity.ok(response);
        });
    }


    @PostMapping("/refresh")
    public Mono<ResponseEntity<Map<String, String>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshRequest) {
        return Mono.fromCallable(() -> {
            String refreshToken = refreshRequest.getRefreshToken();
            
            if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Refresh token invalide");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String username = jwtUtil.extractUsername(refreshToken);
            Long userId = jwtUtil.extractUserId(refreshToken);
            UserInfo user = users.get(username);
            
            if (user == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Utilisateur non trouvé");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            }

            String newAccessToken = jwtUtil.generateToken(username, user.getRoles(), userId);
            
            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newAccessToken);
            response.put("message", "Token rafraîchi avec succès");
            
            return ResponseEntity.ok(response);
        });
    }


    @PostMapping("/validate")
    public Mono<ResponseEntity<Map<String, Object>>> validateToken(@RequestParam String token) {
        return Mono.fromCallable(() -> {
            Map<String, Object> response = new HashMap<>();
            
            if (jwtUtil.validateToken(token) && jwtUtil.isAccessToken(token)) {
                String username = jwtUtil.extractUsername(token);
                Long userId = jwtUtil.extractUserId(token);
                List<String> roles = jwtUtil.extractRoles(token);
                
                response.put("valid", true);
                response.put("username", username);
                response.put("userId", userId);
                response.put("roles", roles);
                
                return ResponseEntity.ok(response);
            } else {
                response.put("valid", false);
                response.put("message", "Token invalide");
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        });
    }


    @PostMapping("/logout")
    public Mono<ResponseEntity<Map<String, String>>> logout() {
        return Mono.fromCallable(() -> {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Déconnexion réussie");
            return ResponseEntity.ok(response);
        });
    }


    private Map<String, UserInfo> initializeUsers() {
        Map<String, UserInfo> userMap = new HashMap<>();
        

        userMap.put("admin", new UserInfo(
                1L, "admin", passwordEncoder.encode("admin123"), 
                Arrays.asList("ADMIN", "USER")
        ));
        

        userMap.put("doctor", new UserInfo(
                2L, "doctor", passwordEncoder.encode("doctor123"), 
                Arrays.asList("DOCTOR", "USER")
        ));
        

        userMap.put("user", new UserInfo(
                3L, "user", passwordEncoder.encode("user123"), 
                Arrays.asList("USER")
        ));
        
        return userMap;
    }


    private static class UserInfo {
        private final Long id;
        private final String username;
        private final String password;
        private final List<String> roles;

        public UserInfo(Long id, String username, String password, List<String> roles) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.roles = roles;
        }

        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public List<String> getRoles() { return roles; }
    }
}