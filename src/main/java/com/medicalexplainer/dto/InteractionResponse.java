package com.medicalexplainer.dto;

import java.util.ArrayList;
import java.util.List;

public class InteractionResponse {
    private boolean interactionFound;
    private int totalInteractions;
    private List<InteractionItem> interactions = new ArrayList<>();
    private String generalSummary;
    private String disclaimer;

    public InteractionResponse() {}

    public static class InteractionItem {
        private String medicineA;
        private String medicineB;
        private String severity; // High, Moderate, Mild
        private String description;
        private String recommendation;

        public InteractionItem() {}

        public InteractionItem(String medicineA, String medicineB, String severity, String description, String recommendation) {
            this.medicineA = medicineA;
            this.medicineB = medicineB;
            this.severity = severity;
            this.description = description;
            this.recommendation = recommendation;
        }

        public String getMedicineA() { return medicineA; }
        public void setMedicineA(String medicineA) { this.medicineA = medicineA; }
        public String getMedicineB() { return medicineB; }
        public void setMedicineB(String medicineB) { this.medicineB = medicineB; }
        public String getSeverity() { return severity; }
        public void setSeverity(String severity) { this.severity = severity; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getRecommendation() { return recommendation; }
        public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    }

    public boolean isInteractionFound() { return interactionFound; }
    public void setInteractionFound(boolean interactionFound) { this.interactionFound = interactionFound; }
    public int getTotalInteractions() { return totalInteractions; }
    public void setTotalInteractions(int totalInteractions) { this.totalInteractions = totalInteractions; }
    public List<InteractionItem> getInteractions() { return interactions; }
    public void setInteractions(List<InteractionItem> interactions) { this.interactions = interactions; }
    public String getGeneralSummary() { return generalSummary; }
    public void setGeneralSummary(String generalSummary) { this.generalSummary = generalSummary; }
    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
}
