package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitRequestDTO {

    private Long expenseId;
    private BigDecimal totalAmount;
    private List<SplitUserDTO> splits;
}