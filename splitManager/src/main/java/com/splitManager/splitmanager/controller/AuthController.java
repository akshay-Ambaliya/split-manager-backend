package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.request.LoginRequest;
import com.splitManager.splitmanager.dto.request.RegisterRequest;
import com.splitManager.splitmanager.dto.response.AuthResponse;
import com.splitManager.splitmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return service.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return service.login(request);
    }
}