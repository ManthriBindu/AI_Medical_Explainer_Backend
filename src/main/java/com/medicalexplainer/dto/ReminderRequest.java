package com.medicalexplainer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReminderRequest {

    @NotNull(message = "Medicine ID is required")
    private Long medicineId;

    @NotBlank(message = "Reminder time is required (e.g. 08:00 AM)")
    private String reminderTime;

    private String status = "Upcoming"; // Upcoming, Taken, Missed, Skipped

    public ReminderRequest() {}

    public Long getMedicineId() { return medicineId; }
    public void setMedicineId(Long medicineId) { this.medicineId = medicineId; }
    public String getReminderTime() { return reminderTime; }
    public void setReminderTime(String reminderTime) { this.reminderTime = reminderTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
