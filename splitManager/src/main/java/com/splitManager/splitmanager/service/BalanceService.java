package com.splitManager.splitmanager.service;


import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.BalanceResponseDTO;
import com.splitManager.splitmanager.dto.response.GroupBalanceDTO;

import java.util.List;

public interface BalanceService {

    ApiResponse<BalanceResponseDTO> getMyBalance(String email);


    ApiResponse<BalanceResponseDTO> getUserBalance(Long userId);
}