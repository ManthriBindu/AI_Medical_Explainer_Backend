package com.medicalexplainer.dto;

import java.time.LocalDateTime;

public class ReminderResponse {
    private Long id;
    private Long medicineId;
    private String medicineName;
    private String dosage;
    private String foodInstruction;
    private String reminderTime;
    private String status;
    private LocalDateTime createdAt;

    public ReminderResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getFoodInstruction() { return foodInstruction; }
    public void setFoodInstruction(String foodInstruction) { this.foodInstruction = foodInstruction; }
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
