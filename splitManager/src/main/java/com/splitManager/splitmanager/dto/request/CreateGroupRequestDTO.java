package com.splitManager.splitmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequestDTO {

    @NotBlank(message = "Group name is required")
    private String groupName;
    private String description;
}