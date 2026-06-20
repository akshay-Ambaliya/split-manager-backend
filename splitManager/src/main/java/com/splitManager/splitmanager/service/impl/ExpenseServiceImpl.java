package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.request.CreateExpenseRequestDTO;
import com.splitManager.splitmanager.dto.request.ExpenseUpdateRequestDTO;
import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.ExpenseResponseDTO;
import com.splitManager.splitmanager.dto.response.GroupMemberResponseDTO;
import com.splitManager.splitmanager.entity.*;
import com.splitManager.splitmanager.exception.BadRequestException;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.*;
import com.splitManager.splitmanager.service.ExpenseService;
import com.splitManager.splitmanager.split.SplitStrategy;
import com.splitManager.splitmanager.split.SplitStrategyFactory;
import com.splitManager.splitmanager.split.SplitValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository splitRepository;
    private final SplitValidationService validationService;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final SplitStrategyFactory splitStrategyFactory;

    @Override
    public ApiResponse<Object> createExpense(CreateExpenseRequestDTO request, String email) {

        User paidBy = userRepository.findById(request.getPaidBy())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Expense expense = Expense.builder()
                .group(group)
                .paidBy(paidBy)
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .totalAmount(request.getAmount())
                .splitType(request.getSplitType())
                .expenseDate(LocalDateTime.now())
                .build();

        expense = expenseRepository.save(expense);

        SplitRequestDTO splitRequest = SplitRequestDTO.builder()
                .expenseId(expense.getId())
                .totalAmount(expense.getTotalAmount())
                .splits(request.getSplits())
                .build();

        String type = request.getSplitType().name();

        // 1. Validate
        validationService.validate(type, splitRequest);

        // 2. Get Strategy
        SplitStrategy strategy = splitStrategyFactory.getStrategy(type);

        // 3. Calculate splits
        List<ExpenseSplit> splits = strategy.calculateSplits(splitRequest);

        // 4. Attach expense + persist
        for (ExpenseSplit split : splits) {
            split.setExpense(expense);
        }

        splitRepository.saveAll(splits);


        HashMap<String,Object> response = new HashMap<>();
        response.put("expenseId",expense.getId());
        response.put("message","Expense added successfully");

        return ApiResponse.builder()
                .success(true)
                .message("Expense added successfully")
                .status(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<ExpenseResponseDTO> getExpense(Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        ExpenseResponseDTO dto = mapToDTO(expense);

        return ApiResponse.<ExpenseResponseDTO>builder()
                .success(true)
                .message("Expense fetched successfully")
                .status(HttpStatus.OK)
                .data(dto)
                .build();
    }


    @Override
    @Transactional
    public ApiResponse<ExpenseResponseDTO> updateExpense(Long expenseId,
                                                         ExpenseUpdateRequestDTO request) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (request.getTitle() != null)
            expense.setTitle(request.getTitle());

        if (request.getDescription() != null)
            expense.setDescription(request.getDescription());

        if (request.getCategory() != null)
            expense.setCategory(request.getCategory());

        if (request.getAmount() != null) {
            if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("Amount must be greater than 0");
            }
            expense.setTotalAmount(request.getAmount());
        }

        Expense saved = expenseRepository.save(expense);

        return ApiResponse.success(
                "Expense updated successfully",
                mapToDTO(saved)
        );
    }

    @Override
    @Transactional
    public ApiResponse<String> deleteExpense(Long expenseId) {

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        expenseRepository.delete(expense);

        return ApiResponse.success("Expense deleted successfully", "Deleted");
    }

    private ExpenseResponseDTO mapToDTO(Expense expense) {

        return ExpenseResponseDTO.builder()
                .expenseId(expense.getId())
                .groupId(expense.getGroup().getId())
                .title(expense.getTitle())
                .description(expense.getDescription())
                .category(expense.getCategory())
                .amount(expense.getTotalAmount())
                .paidBy(expense.getPaidBy().getId())
                .paidByName(expense.getPaidBy().getFullName())
                .splitType(expense.getSplitType().name())
                .expenseDate(expense.getExpenseDate())
                .build();
    }

    @Override
    public ApiResponse<Page<ExpenseResponseDTO>> getGroupExpenses(
            Long groupId,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Expense> expenses = expenseRepository.findByGroupId(groupId, pageable);

        Page<ExpenseResponseDTO> dtoPage = expenses.map(expense ->
                ExpenseResponseDTO.builder()
                        .expenseId(expense.getId())
                        .title(expense.getTitle())
                        .description(expense.getDescription())
                        .category(expense.getCategory())
                        .amount(expense.getTotalAmount())
                        .paidBy(expense.getPaidBy().getId())
                        .paidByName(expense.getPaidBy().getFullName())
                        .splitType(expense.getSplitType().name())
                        .expenseDate(expense.getExpenseDate())
                        .build()
        );

        return ApiResponse.<Page<ExpenseResponseDTO>>builder()
                .success(true)
                .message("Group expenses fetched successfully")
                .status(HttpStatus.OK)
                .data(dtoPage)
                .build();
    }
}