package com.splitManager.splitmanager.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceResponseDTO {
    private BigDecimal totalReceivable;
    private BigDecimal totalOwed;

    private BigDecimal settlementsPaid;
    private BigDecimal settlementsReceived;

    private BigDecimal netBalance;
}