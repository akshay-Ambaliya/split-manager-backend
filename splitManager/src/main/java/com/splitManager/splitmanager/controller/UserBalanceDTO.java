package com.splitManager.splitmanager.controller;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBalanceDTO {

    private Long userId;
    private String fullName;
    private BigDecimal amount;

}