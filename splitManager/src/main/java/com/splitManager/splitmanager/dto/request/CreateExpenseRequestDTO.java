package com.splitManager.splitmanager.dto.request;


import com.splitManager.splitmanager.enums.SplitType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateExpenseRequestDTO {

    private Long groupId;

    private String title;

    private String description;

    private String category;

    private BigDecimal amount;

    private Long paidBy;

    private SplitType splitType;

    private List<SplitUserDTO> splits;
}