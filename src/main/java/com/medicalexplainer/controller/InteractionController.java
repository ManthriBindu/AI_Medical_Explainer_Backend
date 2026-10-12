package com.medicalexplainer.controller;

import com.medicalexplainer.dto.InteractionRequest;
import com.medicalexplainer.dto.InteractionResponse;
import com.medicalexplainer.service.InteractionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interactions")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @PostMapping("/check")
    public ResponseEntity<InteractionResponse> checkInteractions(@Valid @RequestBody InteractionRequest request) {
        InteractionResponse response = interactionService.checkInteractions(request);
        return ResponseEntity.ok(response);
    }
}
