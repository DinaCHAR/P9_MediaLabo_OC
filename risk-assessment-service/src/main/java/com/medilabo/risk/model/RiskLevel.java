package com.medilabo.risk.model;


public enum RiskLevel {
    NONE("None", "#28a745"),
    BORDERLINE("Borderline", "#ffc107"),
    IN_DANGER("In Danger", "#fd7e14"),
    EARLY_ONSET("Early Onset", "#dc3545");

    private final String description;
    private final String color;

    RiskLevel(String description, String color) {
        this.description = description;
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }
}