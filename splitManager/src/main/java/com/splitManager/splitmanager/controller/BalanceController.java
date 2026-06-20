package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.response.*;
import com.splitManager.splitmanager.security.JwtUtil;
import com.splitManager.splitmanager.service.BalanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<BalanceResponseDTO>> getMyBalance(
            HttpServletRequest request
    ) {
        ApiResponse<BalanceResponseDTO> data = balanceService.getMyBalance(jwtUtil.extractEmailFromRequest(request));
        return ResponseEntity.status(data.getStatus()).body(data);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<BalanceResponseDTO>> getUserBalance(
            @PathVariable Long userId) {

        ApiResponse<BalanceResponseDTO> data = balanceService.getUserBalance(userId);
        return ResponseEntity.status(data.getStatus()).body(data);
    }
}