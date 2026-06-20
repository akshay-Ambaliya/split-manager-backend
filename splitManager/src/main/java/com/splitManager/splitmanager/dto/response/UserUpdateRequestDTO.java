package com.splitManager.splitmanager.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    private String fullName;
    private String profilePicture;
}