package com.splitManager.splitmanager.service;

import com.splitManager.splitmanager.dto.request.CreateExpenseRequestDTO;
import com.splitManager.splitmanager.dto.request.ExpenseUpdateRequestDTO;
import com.splitManager.splitmanager.dto.response.ExpenseResponseDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import org.springframework.data.domain.Page;


public interface ExpenseService {

    ApiResponse<Object> createExpense(CreateExpenseRequestDTO request, String email);
    ApiResponse<ExpenseResponseDTO> getExpense(Long expenseId);

    ApiResponse<ExpenseResponseDTO> updateExpense(Long expenseId,
                                                  ExpenseUpdateRequestDTO request);

    ApiResponse<String> deleteExpense(Long expenseId);

    ApiResponse<Page<ExpenseResponseDTO>> getGroupExpenses(
            Long groupId,
            int page,
            int size,
            String sortBy,
            String direction
    );
}