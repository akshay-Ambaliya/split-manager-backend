package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.request.*;
import com.splitManager.splitmanager.dto.response.SplitResponseDTO;
import com.splitManager.splitmanager.exception.BadRequestException;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.service.SplitService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SplitServiceImpl implements SplitService {

    @Override
    public List<SplitResponseDTO> equalSplit(EqualSplitRequestDTO request) {

        int size = request.getMembers().size();
        double splitAmount = request.getAmount() / size;

        List<SplitResponseDTO> result = new ArrayList<>();

        for (Long userId : request.getMembers()) {
            result.add(SplitResponseDTO.builder()
                    .userId(userId)
                    .amount(splitAmount)
                    .build());
        }

        return result;
    }

    @Override
    public List<SplitResponseDTO> percentageSplit(PercentageSplitRequestDTO request) {

        double total = request.getAmount();
        double sum = request.getSplits()
                .stream()
                .mapToDouble(PercentageSplitRequestDTO.PercentageUserDTO::getPercentage)
                .sum();

        if (sum != 100) {
            throw new BadRequestException("Total percentage must be 100");
        }

        List<SplitResponseDTO> result = new ArrayList<>();

        for (PercentageSplitRequestDTO.PercentageUserDTO split : request.getSplits()) {

            double amount = (split.getPercentage() / 100) * total;

            result.add(SplitResponseDTO.builder()
                    .userId(split.getUserId())
                    .percentage(split.getPercentage())
                    .amount(amount)
                    .build());
        }

        return result;
    }

    @Override
    public List<SplitResponseDTO> exactSplit(ExactSplitRequestDTO request) {

        double total = request.getAmount();

        double sum = request.getSplits()
                .stream()
                .mapToDouble(ExactSplitRequestDTO.ExactUserDTO::getAmount)
                .sum();

        if (sum != total) {
            throw new BadRequestException("Total split amount must equal expense amount");
        }

        List<SplitResponseDTO> result = new ArrayList<>();

        for (ExactSplitRequestDTO.ExactUserDTO split : request.getSplits()) {

            result.add(SplitResponseDTO.builder()
                    .userId(split.getUserId())
                    .amount(split.getAmount())
                    .build());
        }

        return result;
    }
}