package com.splitManager.splitmanager.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseSplitRequestDTO {

    private Long userId;

    private BigDecimal amount;

    private BigDecimal percentage;
}