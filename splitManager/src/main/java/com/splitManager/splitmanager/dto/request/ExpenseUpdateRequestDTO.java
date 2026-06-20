package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseUpdateRequestDTO {

    private String title;
    private String description;
    private String category;
    private BigDecimal amount;
}