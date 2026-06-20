package com.splitManager.splitmanager.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GroupResponseDTO {

    private Long groupId;
    private String groupName;
    private String description;
    private Long createdBy;
}