package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.request.CreateExpenseRequestDTO;
import com.splitManager.splitmanager.dto.request.ExpenseUpdateRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.ExpenseResponseDTO;
import com.splitManager.splitmanager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> createExpense(
            @RequestBody CreateExpenseRequestDTO request,
            Authentication authentication
    ) {
        String email = authentication.getName();

        ApiResponse<Object> response =
                expenseService.createExpense(request, email);

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> getExpense(
            @PathVariable Long expenseId) {

        ApiResponse<ExpenseResponseDTO> response =
                expenseService.getExpense(expenseId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseUpdateRequestDTO request) {

        ApiResponse<ExpenseResponseDTO> response =
                expenseService.updateExpense(expenseId, request);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<ApiResponse<String>> deleteExpense(
            @PathVariable Long expenseId) {

        ApiResponse<String> response =
                expenseService.deleteExpense(expenseId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @GetMapping("/groups/{groupId}/expenses")
    public ResponseEntity<ApiResponse<Page<ExpenseResponseDTO>>> getGroupExpenses(
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "expenseDate") String sort,
            @RequestParam(defaultValue = "desc") String direction
    ) {

        ApiResponse<Page<ExpenseResponseDTO>> response =
                expenseService.getGroupExpenses(groupId, page, size, sort, direction);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }
}