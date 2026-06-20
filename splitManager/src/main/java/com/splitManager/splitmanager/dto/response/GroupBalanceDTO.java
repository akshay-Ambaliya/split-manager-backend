package com.splitManager.splitmanager.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupBalanceDTO {

    private String fromUser;   // debtor
    private String toUser;     // creditor
    private BigDecimal amount;
}