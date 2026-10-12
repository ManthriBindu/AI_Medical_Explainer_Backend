package com.medicalexplainer.dto;

import java.util.ArrayList;
import java.util.List;

public class ReportAnalysisDto {
    private String summary;
    private String reportType;
    private List<TestResultItem> testResults = new ArrayList<>();
    private List<String> importantValues = new ArrayList<>();
    private List<String> abnormalValues = new ArrayList<>();
    private List<TermExplanationItem> simpleExplanations = new ArrayList<>();
    private List<String> questionsForDoctor = new ArrayList<>();
    private String disclaimer;
    private boolean isPrescription;
    private List<ExtractedMedicineDto> extractedMedicines = new ArrayList<>();
    private String prescriptionNotice;

    public ReportAnalysisDto() {}

    public static class TestResultItem {
        private String testName;
        private String yourValue;
        private String referenceRange;
        private String unit;
        private String status; // Normal, Attention, Abnormal
        private String simpleMeaning;

        public TestResultItem() {}

        public TestResultItem(String testName, String yourValue, String referenceRange, String unit, String status, String simpleMeaning) {
            this.testName = testName;
            this.yourValue = yourValue;
            this.referenceRange = referenceRange;
            this.unit = unit;
            this.status = status;
            this.simpleMeaning = simpleMeaning;
        }

        public String getTestName() { return testName; }
        public void setTestName(String testName) { this.testName = testName; }
        public String getYourValue() { return yourValue; }
        public void setYourValue(String yourValue) { this.yourValue = yourValue; }
        public String getReferenceRange() { return referenceRange; }
        public void setReferenceRange(String referenceRange) { this.referenceRange = referenceRange; }
        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getSimpleMeaning() { return simpleMeaning; }
        public void setSimpleMeaning(String simpleMeaning) { this.simpleMeaning = simpleMeaning; }
    }

    public static class TermExplanationItem {
        private String term;
        private String simpleExplanation;
        private String whyItMatters;

        public TermExplanationItem() {}

        public TermExplanationItem(String term, String simpleExplanation, String whyItMatters) {
            this.term = term;
            this.simpleExplanation = simpleExplanation;
            this.whyItMatters = whyItMatters;
        }

        public String getTerm() { return term; }
        public void setTerm(String term) { this.term = term; }
        public String getSimpleExplanation() { return simpleExplanation; }
        public void setSimpleExplanation(String simpleExplanation) { this.simpleExplanation = simpleExplanation; }
        public String getWhyItMatters() { return whyItMatters; }
        public void setWhyItMatters(String whyItMatters) { this.whyItMatters = whyItMatters; }
    }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    public List<TestResultItem> getTestResults() { return testResults; }
    public void setTestResults(List<TestResultItem> testResults) { this.testResults = testResults; }
    public List<String> getImportantValues() { return importantValues; }
    public void setImportantValues(List<String> importantValues) { this.importantValues = importantValues; }
    public List<String> getAbnormalValues() { return abnormalValues; }
    public void setAbnormalValues(List<String> abnormalValues) { this.abnormalValues = abnormalValues; }
    public List<TermExplanationItem> getSimpleExplanations() { return simpleExplanations; }
    public void setSimpleExplanations(List<TermExplanationItem> simpleExplanations) { this.simpleExplanations = simpleExplanations; }
    public List<String> getQuestionsForDoctor() { return questionsForDoctor; }
    public void setQuestionsForDoctor(List<String> questionsForDoctor) { this.questionsForDoctor = questionsForDoctor; }
    public String getDisclaimer() { return disclaimer; }
    public void setDisclaimer(String disclaimer) { this.disclaimer = disclaimer; }
    public boolean isPrescription() { return isPrescription; }
    public void setPrescription(boolean prescription) { isPrescription = prescription; }
    public List<ExtractedMedicineDto> getExtractedMedicines() { return extractedMedicines; }
    public void setExtractedMedicines(List<ExtractedMedicineDto> extractedMedicines) { this.extractedMedicines = extractedMedicines; }
    public String getPrescriptionNotice() { return prescriptionNotice; }
    public void setPrescriptionNotice(String prescriptionNotice) { this.prescriptionNotice = prescriptionNotice; }
}
