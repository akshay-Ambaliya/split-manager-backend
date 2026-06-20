package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitUserDTO {

    private Long userId;
    private BigDecimal percentage; // for percentage split
    private BigDecimal amount;     // for exact split
}