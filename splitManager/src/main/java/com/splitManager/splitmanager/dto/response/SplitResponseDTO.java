package com.splitManager.splitmanager.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitResponseDTO {
    private Long userId;
    private Double amount;
    private Double percentage;
}