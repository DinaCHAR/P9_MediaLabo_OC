package com.medilabo.frontend.controller;

import com.medilabo.frontend.dto.Patient;
import com.medilabo.frontend.dto.Note;
import com.medilabo.frontend.dto.RiskAssessment;
import com.medilabo.frontend.service.PatientService;
import com.medilabo.frontend.service.NoteService;
import com.medilabo.frontend.service.RiskAssessmentService;
import com.medilabo.frontend.service.SimplePageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Controller
public class WebController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private RiskAssessmentService riskAssessmentService;


    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        try {
            SimplePageImpl<Patient> patientsPage = patientService.getAllPatients(0, 1000);
            List<Patient> patients = patientsPage.getContent();
            model.addAttribute("totalPatients", patients.size());
            

            long maleCount = patients.stream().filter(p -> "M".equals(p.getGender())).count();
            long femaleCount = patients.stream().filter(p -> "F".equals(p.getGender())).count();
            
            model.addAttribute("malePatients", maleCount);
            model.addAttribute("femalePatients", femaleCount);
            
        } catch (Exception e) {
            model.addAttribute("totalPatients", 0);
            model.addAttribute("malePatients", 0);
            model.addAttribute("femalePatients", 0);
        }
        return "dashboard";
    }

    @GetMapping("/patients")
    public String patients(Model model, 
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String search) {
        try {
            List<Patient> patients;
            if (search != null && !search.trim().isEmpty()) {
                patients = patientService.searchPatientsByName(search.trim());
            } else {
                SimplePageImpl<Patient> patientsPage = patientService.getAllPatients(page, size);
                patients = patientsPage.getContent();
                model.addAttribute("totalPages", patientsPage.getTotalPages());
                model.addAttribute("hasNext", patientsPage.hasNext());
                model.addAttribute("hasPrevious", patientsPage.hasPrevious());
            }
            model.addAttribute("patients", patients);
            model.addAttribute("currentPage", page);
            model.addAttribute("search", search);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des patients: " + e.getMessage());
        }
        return "patients";
    }

    @GetMapping("/patients/{id}")
    public String patientDetail(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
        } catch (Exception e) {
            model.addAttribute("error", "Patient non trouvé: " + e.getMessage());
            return "redirect:/patients";
        }
        return "patient-detail";
    }

    @GetMapping("/patients/{id}/notes")
    public String patientNotes(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            List<Note> notes = noteService.getNotesByPatientId(id);
            model.addAttribute("patient", patient);
            model.addAttribute("notes", notes);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des notes: " + e.getMessage());
        }
        return "patient-notes";
    }


    
    @GetMapping("/patients/new")
    public String newPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "patient-form";
    }
    
    @GetMapping("/patients/{id}/edit")
    public String editPatient(@PathVariable Long id, Model model) {
        try {
            Patient patient = patientService.getPatientById(id);
            model.addAttribute("patient", patient);
        } catch (Exception e) {
            model.addAttribute("error", "Patient non trouvé: " + e.getMessage());
            return "redirect:/patients";
        }
        return "patient-form";
    }
    
    @PostMapping("/patients")
    public String savePatient(@ModelAttribute Patient patient, RedirectAttributes redirectAttributes, Model model) {
        try {

            if (patient.getDateOfBirth() != null) {
                java.time.LocalDate currentDate = java.time.LocalDate.now();
                java.time.LocalDate minDate = java.time.LocalDate.of(1900, 1, 1);
                
                if (patient.getDateOfBirth().isAfter(currentDate)) {
                    model.addAttribute("error", "La date de naissance ne peut pas être dans le futur.");
                    model.addAttribute("patient", patient);
                    return "patient-form";
                }
                
                if (patient.getDateOfBirth().isBefore(minDate)) {
                    model.addAttribute("error", "Veuillez saisir une date de naissance valide (après 1900).");
                    model.addAttribute("patient", patient);
                    return "patient-form";
                }
            }
            
            if (patient.getId() != null) {
                patientService.updatePatient(patient.getId(), patient);
                redirectAttributes.addFlashAttribute("success", "Patient modifié avec succès");
            } else {
                patientService.createPatient(patient);
                redirectAttributes.addFlashAttribute("success", "Patient créé avec succès");
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("Failed to convert") && errorMessage.contains("dateOfBirth")) {
                model.addAttribute("error", "Format de date invalide. Veuillez utiliser le format JJ/MM/AAAA.");
            } else {
                model.addAttribute("error", "Erreur lors de la sauvegarde: " + errorMessage);
            }
            model.addAttribute("patient", patient);
            return "patient-form";
        }
        return "redirect:/patients";
    }
    
    @PostMapping("/patients/{id}/delete")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            patientService.deletePatient(id);
            redirectAttributes.addFlashAttribute("success", "Patient supprimé avec succès");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la suppression: " + e.getMessage());
        }
        return "redirect:/patients";
    }
    
    @PostMapping("/patients/{patientId}/notes")
    public String saveNote(@PathVariable Long patientId, 
                          @RequestParam String noteContent,
                          @RequestParam(required = false) String redirectToRisk,
                          RedirectAttributes redirectAttributes) {
        try {
            Note note = new Note();
            note.setPatientId(patientId);
            note.setNoteContent(noteContent);
            noteService.createNote(note);
            redirectAttributes.addFlashAttribute("success", "Note ajoutée avec succès");
            
            // Si l'utilisateur souhaite voir l'évaluation des risques
            if ("true".equals(redirectToRisk)) {
                redirectAttributes.addFlashAttribute("info", "Note ajoutée. Vérifiez l'évaluation des risques mise à jour.");
                return "redirect:/risk-assessments";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de l'ajout de la note: " + e.getMessage());
        }
        return "redirect:/patients/" + patientId + "/notes";
    }


    @GetMapping("/risk-assessments")
    public String riskAssessments() {
        return "risk-assessments";
    }


    @GetMapping("/api/risk-assessments")
    @ResponseBody
    public List<RiskAssessment> getAllRiskAssessments() {
        try {
            return riskAssessmentService.getAllRiskAssessments();
        } catch (Exception e) {

            return new ArrayList<>();
        }
    }


    @GetMapping("/api/risk-assessments/{patientId}")
    @ResponseBody
    public RiskAssessment getRiskAssessment(@PathVariable Long patientId) {
        try {
            return riskAssessmentService.getRiskAssessmentForPatient(patientId);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}