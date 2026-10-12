package com.medicalexplainer.controller;

import com.medicalexplainer.dto.ReportResponse;
import com.medicalexplainer.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping({"", "/upload"})
    public ResponseEntity<ReportResponse> uploadReport(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "reportType", required = false) String reportType,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReportResponse response = reportService.uploadAndProcessReport(file, reportType, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/extract-prescription")
    public ResponseEntity<Map<String, Object>> extractPrescription(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Map<String, Object> response = reportService.extractPrescriptionOnly(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReportResponse>> getReports(@AuthenticationPrincipal UserDetails userDetails) {
        List<ReportResponse> reports = reportService.getUserReports(userDetails.getUsername());
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponse> getReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ReportResponse report = reportService.getReportById(id, userDetails.getUsername());
        return ResponseEntity.ok(report);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteReport(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        reportService.deleteReport(id, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Report deleted successfully"));
    }
}
