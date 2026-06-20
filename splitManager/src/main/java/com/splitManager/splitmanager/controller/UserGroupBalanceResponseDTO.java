package com.splitManager.splitmanager.controller;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroupBalanceResponseDTO {

    private Long groupId;
    private String groupName;
    private List<UserBalanceDTO> balances;

}