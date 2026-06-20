package com.splitManager.splitmanager.service;


import com.splitManager.splitmanager.dto.request.LoginRequest;
import com.splitManager.splitmanager.dto.request.RegisterRequest;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.AuthResponse;
import com.splitManager.splitmanager.dto.response.UserResponseDTO;
import com.splitManager.splitmanager.dto.response.UserUpdateRequestDTO;
import com.splitManager.splitmanager.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    ApiResponse<UserResponseDTO> getLoggedInUser();

    ApiResponse<UserResponseDTO> updateLoggedInUser(String email, UserUpdateRequestDTO request);

    ApiResponse<?> searchUsers(String keyword);

    ApiResponse<?> uploadImage(MultipartFile file, String email);
}