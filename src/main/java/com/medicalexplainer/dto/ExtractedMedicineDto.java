package com.medicalexplainer.dto;

import java.util.ArrayList;
import java.util.List;

public class ExtractedMedicineDto {
    private String medicineName;
    private String dosage;
    private String frequency;
    private String foodInstruction;
    private String duration;
    private List<String> reminderTimes = new ArrayList<>();
    private String notes;
    private boolean needsConfirmation;
    private String confirmationReason;

    public ExtractedMedicineDto() {}

    public ExtractedMedicineDto(String medicineName, String dosage, String frequency, String foodInstruction,
                                String duration, List<String> reminderTimes, String notes,
                                boolean needsConfirmation, String confirmationReason) {
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.frequency = frequency;
        this.foodInstruction = foodInstruction;
        this.duration = duration;
        this.reminderTimes = reminderTimes != null ? reminderTimes : new ArrayList<>();
        this.notes = notes;
        this.needsConfirmation = needsConfirmation;
        this.confirmationReason = confirmationReason;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFoodInstruction() {
        return foodInstruction;
    }

    public void setFoodInstruction(String foodInstruction) {
        this.foodInstruction = foodInstruction;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(List<String> reminderTimes) {
        this.reminderTimes = reminderTimes != null ? reminderTimes : new ArrayList<>();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isNeedsConfirmation() {
        return needsConfirmation;
    }

    public void setNeedsConfirmation(boolean needsConfirmation) {
        this.needsConfirmation = needsConfirmation;
    }

    public String getConfirmationReason() {
        return confirmationReason;
    }

    public void setConfirmationReason(String confirmationReason) {
        this.confirmationReason = confirmationReason;
    }
}
