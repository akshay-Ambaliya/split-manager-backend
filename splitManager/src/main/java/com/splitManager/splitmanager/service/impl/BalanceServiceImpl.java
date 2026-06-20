package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.BalanceResponseDTO;
import com.splitManager.splitmanager.dto.response.DebtDTO;
import com.splitManager.splitmanager.entity.Expense;
import com.splitManager.splitmanager.entity.ExpenseSplit;
import com.splitManager.splitmanager.entity.Group;
import com.splitManager.splitmanager.entity.GroupMember;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.ExpenseRepository;
import com.splitManager.splitmanager.repository.ExpenseSplitRepository;
import com.splitManager.splitmanager.repository.GroupMemberRepository;
import com.splitManager.splitmanager.repository.GroupRepository;
import com.splitManager.splitmanager.repository.SettlementRepository;
import com.splitManager.splitmanager.repository.UserRepository;
import com.splitManager.splitmanager.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final ExpenseSplitRepository expenseSplitRepository;
    private final SettlementRepository settlementRepository;

    @Override
    public ApiResponse<BalanceResponseDTO> getMyBalance(String email) {

//        Balance =     (get total expenses where payerId = userId)
//                    - (get total expense splits where userId = userId)
//                    - (get total amount received from settlements)
//                    + (get total amount paid in settlements)

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

//      get total expense splits where userId = userId
        BigDecimal owed = expenseSplitRepository.getTotalOwed(user.getId());

//      get total expenses where payerId = userId
        BigDecimal receivable = expenseRepository.getTotalPaidByUser(user.getId());

//      get total amount received from settlements
        BigDecimal settlementsPaid = settlementRepository.getTotalSettledPaid(user.getId());

//      get total amount paid in settlements
        BigDecimal settlementsReceived = settlementRepository.getTotalSettledReceived(user.getId());


        owed = (owed == null) ? BigDecimal.ZERO : owed;
        receivable = (receivable == null) ? BigDecimal.ZERO : receivable;
        settlementsPaid = (settlementsPaid == null) ? BigDecimal.ZERO : settlementsPaid;
        settlementsReceived = (settlementsReceived == null ? BigDecimal.ZERO : settlementsReceived);


        BigDecimal net = receivable
                .subtract(owed)
                .subtract(settlementsReceived)
                .add(settlementsPaid);

        return ApiResponse.<BalanceResponseDTO>builder()
                .success(true)
                .message("Balance Fetched Successfully")
                .data(
                        BalanceResponseDTO.builder()
                                .totalReceivable(receivable)
                                .totalOwed(owed)
                                .settlementsPaid(settlementsPaid)
                                .settlementsReceived(settlementsReceived)
                                .netBalance(net)
                                .build()
                )
                .status(HttpStatus.OK)
                .build();
    }

    @Override
    public ApiResponse<BalanceResponseDTO> getUserBalance(Long userId) {

        BigDecimal owed = expenseSplitRepository.getTotalOwed(userId);
        BigDecimal receivable = expenseRepository.getTotalPaidByUser(userId);

        owed = (owed == null) ? BigDecimal.ZERO : owed;
        receivable = (receivable == null) ? BigDecimal.ZERO : receivable;

        BigDecimal net = receivable.subtract(owed);

        return ApiResponse.<BalanceResponseDTO>builder()
                .success(true)
                .data(
                        BalanceResponseDTO.builder()
                                .totalOwed(owed)
                                .totalReceivable(receivable)
                                .netBalance(net)
                                .build()
                )
                .status(HttpStatus.OK)
                .build();
    }
}