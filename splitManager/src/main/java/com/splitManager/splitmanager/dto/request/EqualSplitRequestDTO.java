package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EqualSplitRequestDTO {
    private Double amount;
    private List<Long> members;
}