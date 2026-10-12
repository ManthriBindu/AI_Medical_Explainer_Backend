package com.medicalexplainer.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class InteractionRequest {

    @NotEmpty(message = "Please provide at least one medicine to check interactions")
    private List<String> medicineNames;

    public InteractionRequest() {}

    public InteractionRequest(List<String> medicineNames) {
        this.medicineNames = medicineNames;
    }

    public List<String> getMedicineNames() {
        return medicineNames;
    }

    public void setMedicineNames(List<String> medicineNames) {
        this.medicineNames = medicineNames;
    }
}
