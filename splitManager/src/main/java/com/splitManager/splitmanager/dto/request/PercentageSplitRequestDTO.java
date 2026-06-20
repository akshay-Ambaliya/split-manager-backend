package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PercentageSplitRequestDTO {

    private Double amount;

    private List<PercentageUserDTO> splits;

    @Getter
    @Setter
    public static class PercentageUserDTO {
        private Long userId;
        private Double percentage;
    }
}