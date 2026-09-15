package com.medilabo.frontend.controller;

import com.medilabo.frontend.dto.LoginRequest;
import com.medilabo.frontend.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


@Controller
public class AuthController {

    private final RestTemplate restTemplate;
    
    @Value("${gateway.url:http://gateway-service:8080}")
    private String gatewayUrl;

    public AuthController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, 
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        try {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);
            

            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                gatewayUrl + "/auth/login", 
                request, 
                LoginResponse.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                LoginResponse loginResponse = response.getBody();
                

                session.setAttribute("accessToken", loginResponse.getAccessToken());
                session.setAttribute("refreshToken", loginResponse.getRefreshToken());
                session.setAttribute("username", loginResponse.getUsername());
                session.setAttribute("roles", loginResponse.getRoles());
                
                redirectAttributes.addFlashAttribute("success", "Connexion réussie");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Identifiants invalides");
                return "redirect:/login";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur de connexion: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Déconnexion réussie");
        return "redirect:/login";
    }
}