package com.splitManager.splitmanager.service;


import com.splitManager.splitmanager.controller.UserBalanceDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.BalanceResponseDTO;
import java.util.List;

public interface BalanceService {

    ApiResponse<BalanceResponseDTO> getMyBalance(String email);


    ApiResponse<BalanceResponseDTO> getUserBalance(Long userId);

    ApiResponse<List<UserBalanceDTO>> getReceivables(Long groupId,String email);

    ApiResponse<List<UserBalanceDTO>> getPayables(Long groupId,String email);

}