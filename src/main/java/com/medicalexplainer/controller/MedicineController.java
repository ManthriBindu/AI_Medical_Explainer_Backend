package com.medicalexplainer.controller;

import com.medicalexplainer.dto.MedicineRequest;
import com.medicalexplainer.dto.MedicineResponse;
import com.medicalexplainer.service.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    public ResponseEntity<MedicineResponse> createMedicine(
            @Valid @RequestBody MedicineRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MedicineResponse response = medicineService.createMedicine(request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<MedicineResponse>> createMedicinesBatch(
            @RequestBody List<MedicineRequest> requests,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MedicineResponse> responses = medicineService.createMedicinesBatch(requests, userDetails.getUsername());
        return ResponseEntity.ok(responses);
    }

    @GetMapping
    public ResponseEntity<List<MedicineResponse>> getMedicines(@AuthenticationPrincipal UserDetails userDetails) {
        List<MedicineResponse> list = medicineService.getUserMedicines(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicineResponse> getMedicine(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MedicineResponse res = medicineService.getMedicineById(id, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicineResponse> updateMedicine(
            @PathVariable Long id,
            @Valid @RequestBody MedicineRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        MedicineResponse res = medicineService.updateMedicine(id, request, userDetails.getUsername());
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMedicine(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        medicineService.deleteMedicine(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Medicine deleted successfully"));
    }
}
