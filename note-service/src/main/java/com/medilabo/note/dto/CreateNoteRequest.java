package com.medilabo.note.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateNoteRequest {

    @NotNull(message = "L'ID du patient est obligatoire")
    private Long patientId;

    @NotBlank(message = "Le contenu de la note est obligatoire")
    private String content;

    public CreateNoteRequest() {}

    public CreateNoteRequest(Long patientId, String content) {
        this.patientId = patientId;
        this.content = content;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}