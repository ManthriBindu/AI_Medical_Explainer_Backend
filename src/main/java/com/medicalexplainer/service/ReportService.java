package com.medicalexplainer.service;

import com.medicalexplainer.dto.ReportAnalysisDto;
import com.medicalexplainer.dto.ReportResponse;
import com.medicalexplainer.entity.MedicalReport;
import com.medicalexplainer.entity.User;
import com.medicalexplainer.exception.BadRequestException;
import com.medicalexplainer.exception.ResourceNotFoundException;
import com.medicalexplainer.repository.ReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    private final ReportRepository reportRepository;
    private final OCRService ocrService;
    private final AIExplanationService aiExplanationService;
    private final AuthService authService;

    public ReportService(ReportRepository reportRepository,
                         OCRService ocrService,
                         AIExplanationService aiExplanationService,
                         AuthService authService) {
        this.reportRepository = reportRepository;
        this.ocrService = ocrService;
        this.aiExplanationService = aiExplanationService;
        this.authService = authService;
    }

    @Transactional
    public ReportResponse uploadAndProcessReport(MultipartFile file, String customReportType, String userEmail) {
        User user = authService.getUserByEmail(userEmail);

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a valid medical report file to upload.");
        }

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "report.pdf";
        String storedFilename = UUID.randomUUID().toString() + "_" + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        Path uploadPath = Paths.get(uploadDir);
        try {
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path destination = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("Failed to save uploaded file: {}", e.getMessage(), e);
            throw new BadRequestException("Failed to save uploaded document. Please try again.");
        }

        // 1. OCR Extraction
        String extractedText = ocrService.extractText(file);

        // 2. AI Explanation / Clinical Processing (with customReportType hint)
        ReportAnalysisDto analysis = aiExplanationService.analyzeMedicalText(extractedText, customReportType);
        String aiSummaryJson = aiExplanationService.toJsonString(analysis);

        // 3. Save into Database
        MedicalReport report = new MedicalReport();
        report.setUser(user);
        report.setFileName(originalFilename);
        report.setFilePath("/uploads/" + storedFilename);
        report.setReportType(customReportType != null && !customReportType.isBlank() ? customReportType : analysis.getReportType());
        report.setExtractedText(extractedText);
        report.setAiSummary(aiSummaryJson);
        report.setStatus("Reviewed");

        MedicalReport saved = reportRepository.save(report);

        return mapToResponse(saved, analysis);
    }

    public java.util.Map<String, Object> extractPrescriptionOnly(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Please select a valid prescription file to extract.");
        }

        String extractedText = ocrService.extractText(file);
        List<com.medicalexplainer.dto.ExtractedMedicineDto> medicines = aiExplanationService.extractMedicinesFromPrescriptionText(extractedText);

        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("fileName", file.getOriginalFilename());
        result.put("extractedText", extractedText);
        result.put("extractedMedicines", medicines);
        if (medicines.isEmpty()) {
            result.put("prescriptionNotice", "No medicine details could be identified in this prescription. Please verify the document or enter details manually.");
        } else {
            result.put("prescriptionNotice", "Successfully extracted " + medicines.size() + " medicine(s). Please review and confirm before saving.");
        }
        return result;
    }

    public List<ReportResponse> getUserReports(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return reportRepository.findByUserIdOrderByUploadDateDesc(user.getId())
                .stream()
                .map(r -> mapToResponse(r, aiExplanationService.fromJsonString(r.getAiSummary())))
                .collect(Collectors.toList());
    }

    public ReportResponse getReportById(Long id, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        MedicalReport report = reportRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        ReportAnalysisDto analysis = aiExplanationService.fromJsonString(report.getAiSummary());
        return mapToResponse(report, analysis);
    }

    @Transactional
    public void deleteReport(Long id, String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        MedicalReport report = reportRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));

        reportRepository.delete(report);
    }

    public long getReportCount(String userEmail) {
        User user = authService.getUserByEmail(userEmail);
        return reportRepository.countByUserId(user.getId());
    }

    private ReportResponse mapToResponse(MedicalReport report, ReportAnalysisDto analysis) {
        ReportResponse response = new ReportResponse();
        response.setId(report.getId());
        response.setFileName(report.getFileName());
        response.setFilePath(report.getFilePath());
        response.setReportType(report.getReportType());
        response.setExtractedText(report.getExtractedText());
        response.setAiSummary(report.getAiSummary());
        response.setUploadDate(report.getUploadDate());
        response.setStatus(report.getStatus());
        response.setAnalysis(analysis);
        return response;
    }
}
