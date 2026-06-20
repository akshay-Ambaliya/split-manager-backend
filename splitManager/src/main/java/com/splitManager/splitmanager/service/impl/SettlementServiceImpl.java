package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.request.CreateSettlementRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.entity.*;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.*;
import com.splitManager.splitmanager.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {

    private final SettlementRepository settlementRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Override
    @Transactional
    public ApiResponse<?> createSettlement(CreateSettlementRequestDTO request) {

        if (request.getAmount().doubleValue() <= 0) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Amount must be greater than zero")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }

        User payer = userRepository.findById(request.getPayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Payer not found"));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        Group group = groupRepository.findById(request.getGroupId())
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        Settlement settlement = new Settlement();
        settlement.setGroup(group);
        settlement.setPayer(payer);
        settlement.setReceiver(receiver);
        settlement.setAmount(request.getAmount());
        settlement.setSettlementDate(LocalDateTime.now());

        settlementRepository.save(settlement);

        return ApiResponse.builder()
                .success(true).message("Settlement Successful").status(HttpStatus.CREATED).data(null).build();
    }

    @Override
    public ApiResponse<?> getAllSettlements(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("settlementDate").descending()
        );

        Page<Settlement> settlementsPage = settlementRepository.findAll(pageable);

        ApiResponse<Page<Settlement>> response = ApiResponse.<Page<Settlement>>builder()
                .success(true)
                .message("Fetched settlements successfully")
                .status(HttpStatus.OK)
                .data(settlementsPage)
                .build();

        return response;
    }
}