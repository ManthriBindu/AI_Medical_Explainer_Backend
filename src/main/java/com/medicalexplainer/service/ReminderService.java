package com.medicalexplainer.service;

import com.medicalexplainer.dto.ReminderRequest;
import com.medicalexplainer.dto.ReminderResponse;
import com.medicalexplainer.entity.MedicationReminder;
import com.medicalexplainer.entity.Medicine;
import com.medicalexplainer.entity.User;
import com.medicalexplainer.exception.BadRequestException;
import com.medicalexplainer.exception.ResourceNotFoundException;
import com.medicalexplainer.repository.MedicineRepository;
import com.medicalexplainer.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final MedicineRepository medicineRepository;
    private final AuthService authService;

    public ReminderService(ReminderRepository reminderRepository,
                           MedicineRepository medicineRepository,
                           AuthService authService) {
        this.reminderRepository = reminderRepository;
        this.medicineRepository = medicineRepository;
        this.authService = authService;
    }

    @Transactional
    public ReminderResponse createReminder(ReminderRequest request, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        Medicine medicine = medicineRepository.findByIdAndUserId(request.getMedicineId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + request.getMedicineId()));

        MedicationReminder reminder = new MedicationReminder(
                medicine,
                request.getReminderTime().trim(),
                request.getStatus() != null ? request.getStatus() : "Upcoming"
        );

        MedicationReminder saved = reminderRepository.save(reminder);
        return mapToResponse(saved);
    }

    public List<ReminderResponse> getTodayReminders(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return reminderRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReminderResponse updateReminderStatus(Long id, String newStatus, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        MedicationReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        if (!reminder.getMedicine().getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to reminder.");
        }

        reminder.setStatus(newStatus);
        MedicationReminder updated = reminderRepository.save(reminder);
        return mapToResponse(updated);
    }

    @Transactional
    public void deleteReminder(Long id, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        MedicationReminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        if (!reminder.getMedicine().getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Unauthorized access to reminder.");
        }

        reminderRepository.delete(reminder);
    }

    public long getUpcomingReminderCount(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return reminderRepository.countUpcomingByUserId(user.getId());
    }

    private ReminderResponse mapToResponse(MedicationReminder reminder) {
        ReminderResponse res = new ReminderResponse();
        res.setId(reminder.getId());
        res.setMedicineId(reminder.getMedicine().getId());
        res.setMedicineName(reminder.getMedicine().getMedicineName());
        res.setDosage(reminder.getMedicine().getDosage());
        res.setFoodInstruction(reminder.getMedicine().getFoodInstruction());
        res.setReminderTime(reminder.getReminderTime());
        res.setStatus(reminder.getStatus());
        res.setCreatedAt(reminder.getCreatedAt());
        return res;
    }
}
