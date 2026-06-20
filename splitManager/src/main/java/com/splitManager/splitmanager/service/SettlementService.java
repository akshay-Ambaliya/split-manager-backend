package com.splitManager.splitmanager.service;

import com.splitManager.splitmanager.dto.request.CreateSettlementRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;

public interface SettlementService {

    ApiResponse<?> createSettlement(CreateSettlementRequestDTO request);

    ApiResponse<?> getAllSettlements(int page, int size);
}