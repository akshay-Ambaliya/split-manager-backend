package com.splitManager.splitmanager.controller;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalanceDTO {

    private Long payToUserId;
    private String payToName;
    private BigDecimal amount;

}