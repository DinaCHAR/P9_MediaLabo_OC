package com.medilabo.frontend.dto;

import java.time.LocalDateTime;
import java.util.List;


public class Note {
    private String id;
    private Long patientId;
    private String noteContent;
    private LocalDateTime createdDate;
    private String practitionerName;
    private List<String> keywords;


    public Note() {}

    public Note(Long patientId, String noteContent, String practitionerName) {
        this.patientId = patientId;
        this.noteContent = noteContent;
        this.practitionerName = practitionerName;
        this.createdDate = LocalDateTime.now();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public void setPractitionerName(String practitionerName) {
        this.practitionerName = practitionerName;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", patientId=" + patientId +
                ", noteContent='" + noteContent + '\'' +
                ", createdDate=" + createdDate +
                ", practitionerName='" + practitionerName + '\'' +
                ", keywords=" + keywords +
                '}';
    }
}