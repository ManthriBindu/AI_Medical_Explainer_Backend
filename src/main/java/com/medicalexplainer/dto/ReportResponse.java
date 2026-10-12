package com.medicalexplainer.dto;

import java.time.LocalDateTime;

public class ReportResponse {
    private Long id;
    private String fileName;
    private String filePath;
    private String reportType;
    private String extractedText;
    private String aiSummary;
    private LocalDateTime uploadDate;
    private String status;
    private ReportAnalysisDto analysis;

    public ReportResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public String getExtractedText() { return extractedText; }
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }
    public String getAiSummary() { return aiSummary; }
    public void setAiSummary(String aiSummary) { this.aiSummary = aiSummary; }
    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public ReportAnalysisDto getAnalysis() { return analysis; }
    public void setAnalysis(ReportAnalysisDto analysis) { this.analysis = analysis; }
}
