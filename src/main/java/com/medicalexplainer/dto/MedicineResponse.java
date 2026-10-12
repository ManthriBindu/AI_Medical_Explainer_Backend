package com.medicalexplainer.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicineResponse {
    private Long id;
    private String medicineName;
    private String dosage;
    private String frequency;
    private String foodInstruction;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private LocalDateTime createdAt;
    private List<ReminderResponse> reminders = new ArrayList<>();
    private String nextReminder;

    public MedicineResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public String getFoodInstruction() { return foodInstruction; }
    public void setFoodInstruction(String foodInstruction) { this.foodInstruction = foodInstruction; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<ReminderResponse> getReminders() { return reminders; }
    public void setReminders(List<ReminderResponse> reminders) { this.reminders = reminders; }
    public String getNextReminder() { return nextReminder; }
    public void setNextReminder(String nextReminder) { this.nextReminder = nextReminder; }
}
