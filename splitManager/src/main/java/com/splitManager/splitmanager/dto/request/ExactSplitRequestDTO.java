package com.splitManager.splitmanager.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExactSplitRequestDTO {

    private Double amount;

    private List<ExactUserDTO> splits;

    @Getter
    @Setter
    public static class ExactUserDTO {
        private Long userId;
        private Double amount;
    }
}