package com.splitManager.splitmanager.dto.request;

import lombok.*;

@Getter
@Setter
public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
}