package com.splitManager.splitmanager.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateSettlementRequestDTO {

    private Long groupId;
    private Long payerId;
    private Long receiverId;
    private BigDecimal amount;
}