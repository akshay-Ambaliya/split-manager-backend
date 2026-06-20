package com.splitManager.splitmanager.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponseDTO {

    private Long expenseId;
    private Long groupId;
    private String title;
    private String description;
    private String category;
    private BigDecimal amount;
    private Long paidBy;
    private String paidByName;
    private String splitType;
    private LocalDateTime expenseDate;
}