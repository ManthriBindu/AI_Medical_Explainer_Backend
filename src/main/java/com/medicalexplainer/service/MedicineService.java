package com.medicalexplainer.service;

import com.medicalexplainer.dto.MedicineRequest;
import com.medicalexplainer.dto.MedicineResponse;
import com.medicalexplainer.dto.ReminderResponse;
import com.medicalexplainer.entity.MedicationReminder;
import com.medicalexplainer.entity.Medicine;
import com.medicalexplainer.entity.User;
import com.medicalexplainer.exception.ResourceNotFoundException;
import com.medicalexplainer.repository.MedicineRepository;
import com.medicalexplainer.repository.ReminderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final ReminderRepository reminderRepository;
    private final AuthService authService;

    public MedicineService(MedicineRepository medicineRepository,
                           ReminderRepository reminderRepository,
                           AuthService authService) {
        this.medicineRepository = medicineRepository;
        this.reminderRepository = reminderRepository;
        this.authService = authService;
    }

    @Transactional
    public MedicineResponse createMedicine(MedicineRequest request, String userEmail) {
        User user = authService.getUserByEmail(userEmail);

        Medicine medicine = new Medicine();
        medicine.setUser(user);
        medicine.setMedicineName(request.getMedicineName().trim());
        medicine.setDosage(request.getDosage().trim());
        medicine.setFrequency(request.getFrequency().trim());
        medicine.setFoodInstruction(request.getFoodInstruction() != null ? request.getFoodInstruction() : "After Food");
        medicine.setStartDate(request.getStartDate());
        medicine.setEndDate(request.getEndDate());
        medicine.setNotes(request.getNotes());

        Medicine saved = medicineRepository.save(medicine);

        // Auto-create reminders if timings are specified
        if (request.getReminderTimes() != null && !request.getReminderTimes().isEmpty()) {
            for (String time : request.getReminderTimes()) {
                if (time != null && !time.isBlank()) {
                    MedicationReminder reminder = new MedicationReminder(saved, time.trim(), "Upcoming");
                    reminderRepository.save(reminder);
                }
            }
        } else {
            // Default morning reminder if none provided
            MedicationReminder reminder = new MedicationReminder(saved, "08:00 AM", "Upcoming");
            reminderRepository.save(reminder);
        }

        return mapToResponse(saved);
    }

    @Transactional
    public List<MedicineResponse> createMedicinesBatch(List<MedicineRequest> requests, String userEmail) {
        if (requests == null || requests.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<MedicineResponse> responses = new java.util.ArrayList<>();
        for (MedicineRequest req : requests) {
            if (req.getMedicineName() != null && !req.getMedicineName().isBlank()) {
                if (req.getDosage() == null || req.getDosage().isBlank()) {
                    req.setDosage("As prescribed");
                }
                if (req.getFrequency() == null || req.getFrequency().isBlank()) {
                    req.setFrequency("Once daily");
                }
                responses.add(createMedicine(req, userEmail));
            }
        }
        return responses;
    }

    public List<MedicineResponse> getUserMedicines(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return medicineRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public MedicineResponse getMedicineById(Long id, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        Medicine medicine = medicineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));
        return mapToResponse(medicine);
    }

    @Transactional
    public MedicineResponse updateMedicine(Long id, MedicineRequest request, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        Medicine medicine = medicineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setMedicineName(request.getMedicineName().trim());
        medicine.setDosage(request.getDosage().trim());
        medicine.setFrequency(request.getFrequency().trim());
        medicine.setFoodInstruction(request.getFoodInstruction() != null ? request.getFoodInstruction() : "After Food");
        medicine.setStartDate(request.getStartDate());
        medicine.setEndDate(request.getEndDate());
        medicine.setNotes(request.getNotes());

        Medicine updated = medicineRepository.save(medicine);

        // Update reminders if new list provided
        if (request.getReminderTimes() != null && !request.getReminderTimes().isEmpty()) {
            List<MedicationReminder> existingReminders = reminderRepository.findByMedicineId(updated.getId());
            reminderRepository.deleteAll(existingReminders);

            for (String time : request.getReminderTimes()) {
                if (time != null && !time.isBlank()) {
                    MedicationReminder reminder = new MedicationReminder(updated, time.trim(), "Upcoming");
                    reminderRepository.save(reminder);
                }
            }
        }

        return mapToResponse(updated);
    }

    @Transactional
    public void deleteMedicine(Long id, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        Medicine medicine = medicineRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicineRepository.delete(medicine);
    }

    public long getMedicineCount(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return medicineRepository.countByUserId(user.getId());
    }

    private MedicineResponse mapToResponse(Medicine medicine) {
        MedicineResponse res = new MedicineResponse();
        res.setId(medicine.getId());
        res.setMedicineName(medicine.getMedicineName());
        res.setDosage(medicine.getDosage());
        res.setFrequency(medicine.getFrequency());
        res.setFoodInstruction(medicine.getFoodInstruction());
        res.setStartDate(medicine.getStartDate());
        res.setEndDate(medicine.getEndDate());
        res.setNotes(medicine.getNotes());
        res.setCreatedAt(medicine.getCreatedAt());

        List<MedicationReminder> reminders = reminderRepository.findByMedicineId(medicine.getId());
        List<ReminderResponse> reminderResponses = reminders.stream().map(r -> {
            ReminderResponse rr = new ReminderResponse();
            rr.setId(r.getId());
            rr.setMedicineId(medicine.getId());
            rr.setMedicineName(medicine.getMedicineName());
            rr.setDosage(medicine.getDosage());
            rr.setFoodInstruction(medicine.getFoodInstruction());
            rr.setReminderTime(r.getReminderTime());
            rr.setStatus(r.getStatus());
            rr.setCreatedAt(r.getCreatedAt());
            return rr;
        }).collect(Collectors.toList());

        res.setReminders(reminderResponses);
        if (!reminderResponses.isEmpty()) {
            res.setNextReminder(reminderResponses.get(0).getReminderTime());
        } else {
            res.setNextReminder("Daily");
        }
        return res;
    }
}
