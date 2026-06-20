package com.splitManager.splitmanager.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponseDTO {

    private Long userId;
    private String fullName;
    private String email;
}