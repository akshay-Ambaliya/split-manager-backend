package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.request.CreateSettlementRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createSettlement(
            @RequestBody CreateSettlementRequestDTO request
    ) {
        ApiResponse<?> response = settlementService.createSettlement(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllSettlements(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ApiResponse<?> response = settlementService.getAllSettlements(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}