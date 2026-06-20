package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.UserResponseDTO;
import com.splitManager.splitmanager.dto.response.UserUpdateRequestDTO;
import com.splitManager.splitmanager.security.JwtUtil;
import com.splitManager.splitmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> getLoggedInUser() {
        ApiResponse<UserResponseDTO> apiResponse = userService.getLoggedInUser();
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<?>> updateLoggedInUser(
            @RequestBody UserUpdateRequestDTO request,
            HttpServletRequest httpServletRequest
    ) {
        String email = jwtUtil.extractEmailFromRequest(httpServletRequest);
        ApiResponse<UserResponseDTO> apiResponse =
                userService.updateLoggedInUser(email, request);

        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchUsers(
            @RequestParam String keyword
    ) {
        ApiResponse<?> response = userService.searchUsers(keyword);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<?>> uploadImage(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request
    ){
        String email = jwtUtil.extractEmailFromRequest(request);
        ApiResponse<?> response = userService.uploadImage(file,email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}