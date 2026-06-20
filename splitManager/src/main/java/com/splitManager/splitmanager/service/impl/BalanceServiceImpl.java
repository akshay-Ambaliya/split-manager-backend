package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.controller.UserBalanceDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.BalanceResponseDTO;
import com.splitManager.splitmanager.entity.*;
import com.splitManager.splitmanager.repository.*;
import com.splitManager.splitmanager.service.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;

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
        BigDecimal owed = orZero(expenseSplitRepository.getTotalOwed(user.getId()));

//      get total expenses where payerId = userId
        BigDecimal receivable = orZero(expenseRepository.getTotalPaidByUser(user.getId()));

//      get total amount paid from settlements
        BigDecimal settlementsPaid = orZero(settlementRepository.getTotalSettledPaid(user.getId()));

//      get total amount received in settlements
        BigDecimal settlementsReceived = orZero(settlementRepository.getTotalSettledReceived(user.getId()));

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

        BigDecimal owed = orZero(expenseSplitRepository.getTotalOwed(userId));
        BigDecimal receivable = orZero(expenseRepository.getTotalPaidByUser(userId));

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

    @Override
    public ApiResponse<List<UserBalanceDTO>> getReceivables(Long groupId, String email) {
        return buildBalanceList(groupId, email, true);
    }

    @Override
    public ApiResponse<List<UserBalanceDTO>> getPayables(Long groupId, String email) {
        return buildBalanceList(groupId, email, false);
    }

    /**
     * Shared logic for both getReceivables and getPayables, since they only differ
     * in the direction passed to getOutstandingAmount and the response message.
     *
     * isReceivable = true  -> how much other members owe the logged-in user
     * isReceivable = false -> how much the logged-in user owes other members
     */
    private ApiResponse<List<UserBalanceDTO>> buildBalanceList(Long groupId, String email, boolean isReceivable) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        List<GroupMember> members = groupMemberRepository.findByGroup(group);

        List<UserBalanceDTO> result = new ArrayList<>();

        for (GroupMember member : members) {

            User otherUser = member.getUser();

            // Skip logged-in user
            if (otherUser.getId().equals(user.getId())) {
                continue;
            }

            // isReceivable: how much other user owes logged-in user
            // !isReceivable: how much logged-in user owes the other user
            BigDecimal amount = isReceivable
                    ? getOutstandingAmount(otherUser.getId(), user.getId(), groupId)
                    : getOutstandingAmount(user.getId(), otherUser.getId(), groupId);

            if (amount.compareTo(BigDecimal.ZERO) > 0) {
                result.add(
                        UserBalanceDTO.builder()
                                .userId(otherUser.getId())
                                .fullName(otherUser.getFullName())
                                .amount(amount)
                                .build()
                );
            }
        }

        return ApiResponse.<List<UserBalanceDTO>>builder()
                .data(result)
                .status(HttpStatus.OK)
                .message(isReceivable ? "Receivables fetched successfully" : "Payable fetched successfully")
                .success(true)
                .build();
    }

    /**
     * Calculates the remaining amount that {@code payerId} owes {@code receiverId} in the given group.
     *
     * Usage:
     * - To know how much User A needs to pay User B:
     *   getOutstandingAmount(userAId, userBId, groupId)
     * - To know how much User A needs to receive from User B:
     *   getOutstandingAmount(userBId, userAId, groupId)
     */
    public BigDecimal getOutstandingAmount(Long payerId, Long receiverId, Long groupId) {

        List<Expense> expenses = expenseRepository.findByGroupId(groupId);

        if (expenses.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal amount = BigDecimal.ZERO;

        for (Expense expense : expenses) {

            if (!expense.getPaidBy().getId().equals(receiverId)) {
                continue;
            }

            List<ExpenseSplit> splits =
                    expenseSplitRepository.findByExpenseId(expense.getId());

            for (ExpenseSplit split : splits) {

                if (split.getUser().getId().equals(payerId)) {
                    amount = amount.add(split.getAmount());
                }
            }
        }

        // Subtract settlements already made
        BigDecimal settledAmount = settlementRepository
                .findByPayerIdAndReceiverIdAndGroupId(payerId, receiverId, groupId)
                .stream()
                .map(Settlement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        amount = amount.subtract(settledAmount);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            amount = BigDecimal.ZERO;
        }

        return amount;
    }

    private BigDecimal orZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}