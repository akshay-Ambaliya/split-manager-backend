package com.splitManager.splitmanager.split;

import com.splitManager.splitmanager.dto.request.SplitRequestDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class SplitValidationService {

    public void validate(String type, SplitRequestDTO request) {

        if (request.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        switch (type.toUpperCase()) {

            case "PERCENTAGE":
                validatePercentage(request);
                break;

            case "EXACT":
                validateExact(request);
                break;
        }
    }

    private void validatePercentage(SplitRequestDTO request) {

        BigDecimal total = request.getSplits()
                .stream()
                .map(s -> s.getPercentage())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new RuntimeException("Total percentage must be 100%");
        }
    }

    private void validateExact(SplitRequestDTO request) {

        BigDecimal total = request.getSplits()
                .stream()
                .map(s -> s.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(request.getTotalAmount()) != 0) {
            throw new RuntimeException("Split amounts must equal total expense");
        }
    }
}