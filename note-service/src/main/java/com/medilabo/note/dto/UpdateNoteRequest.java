package com.medilabo.note.dto;

import javax.validation.constraints.NotBlank;


public class UpdateNoteRequest {

    @NotBlank(message = "Le contenu de la note est obligatoire")
    private String content;

    public UpdateNoteRequest() {}

    public UpdateNoteRequest(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}