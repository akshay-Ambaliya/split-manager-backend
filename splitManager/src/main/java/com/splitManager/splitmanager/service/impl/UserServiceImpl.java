package com.splitManager.splitmanager.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.splitManager.splitmanager.dto.request.LoginRequest;
import com.splitManager.splitmanager.dto.request.RegisterRequest;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.AuthResponse;
import com.splitManager.splitmanager.dto.response.UserResponseDTO;
import com.splitManager.splitmanager.dto.response.UserUpdateRequestDTO;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.exception.BadRequestException;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.UserRepository;
import com.splitManager.splitmanager.security.JwtUtil;
import com.splitManager.splitmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final Cloudinary cloudinary;

    public String register(RegisterRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .build();

        repo.save(user);
        return "User registered successfully";
    }

    public AuthResponse login(LoginRequest request) {
        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail());
    }


    public ApiResponse<UserResponseDTO> getLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED,
                    "User not authenticated",
                    null
            );
        }

        User user = (User) authentication.getPrincipal();

        UserResponseDTO dto = UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();

        return new ApiResponse<>(
                HttpStatus.OK,
                "User fetched successfully",
                dto
        );
    }

    @Override
    public ApiResponse<UserResponseDTO> updateLoggedInUser(String email, UserUpdateRequestDTO request) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName());
        }

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        user.setUpdatedAt(LocalDateTime.now());

        User updatedUser = repo.save(user);

        UserResponseDTO response = UserResponseDTO.builder()
                .id(updatedUser.getId())
                .fullName(updatedUser.getFullName())
                .email(updatedUser.getEmail())
                .profilePicture(updatedUser.getProfilePicture())
                .build();

        return ApiResponse.<UserResponseDTO>builder()
                .success(true)
                .message("Profile updated successfully")
                .status(HttpStatus.OK)
                .data(response)
                .build();
    }

    @Override
    public ApiResponse<?> searchUsers(String keyword) {
        List<User> users = repo
                .findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);

        List<UserResponseDTO> result = users.stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .build())
                .toList();

        return ApiResponse.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Users fetched successfully")
                .status(HttpStatus.OK)
                .data(result)
                .build();
    }

    public String uploadImageOnCloud(MultipartFile file) {

        try {

            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap()
            );

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public ApiResponse<?> uploadImage(MultipartFile file,String email) {

        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String imageUrl = uploadImageOnCloud(file);

        user.setProfilePicture(imageUrl);

        repo.save(user);

        return ApiResponse.<List<UserResponseDTO>>builder()
                .success(true)
                .message("Image uploaded successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build();
    }
}