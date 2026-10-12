package com.medicalexplainer.controller;

import com.medicalexplainer.dto.ReminderRequest;
import com.medicalexplainer.dto.ReminderResponse;
import com.medicalexplainer.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ReminderResponse> createReminder(
            @Valid @RequestBody ReminderRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReminderResponse res = reminderService.createReminder(request, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAllReminders(@AuthenticationPrincipal UserDetails userDetails) {
        List<ReminderResponse> list = reminderService.getTodayReminders(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/today")
    public ResponseEntity<List<ReminderResponse>> getTodayReminders(@AuthenticationPrincipal UserDetails userDetails) {
        List<ReminderResponse> list = reminderService.getTodayReminders(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> updateReminderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String status = body.getOrDefault("status", "Taken");
        ReminderResponse res = reminderService.updateReminderStatus(id, status, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReminder(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reminderService.deleteReminder(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Reminder deleted successfully"));
    }
}
