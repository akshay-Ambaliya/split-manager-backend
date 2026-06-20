package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.request.CreateGroupRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupResponseDTO;
import com.splitManager.splitmanager.security.JwtUtil;
import com.splitManager.splitmanager.service.GroupService;
import com.splitManager.splitmanager.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
@RequiredArgsConstructor
@Slf4j
public class GroupController {

    private final GroupService groupService;
    private final JwtUtil jwtUtil;


    @PostMapping
    public ResponseEntity<ApiResponse<GroupResponseDTO>> createGroup(
            @RequestBody CreateGroupRequestDTO request,
            HttpServletRequest httpServletRequest) {

        String  email = jwtUtil.extractEmailFromRequest(httpServletRequest);

        ApiResponse<GroupResponseDTO> response = groupService.createGroup(request, email);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupResponseDTO>>> getMyGroups(
            HttpServletRequest httpServletRequest
    ) {

        String  email = jwtUtil.extractEmailFromRequest(httpServletRequest);

        ApiResponse<List<GroupResponseDTO>> response = groupService.getMyGroups(email);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<GroupResponseDTO>> getGroup(
            @PathVariable Long groupId) {

        ApiResponse<GroupResponseDTO> response = groupService.getGroupById(groupId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<ApiResponse<GroupResponseDTO>> updateGroup(
            @PathVariable Long groupId,
            @RequestBody CreateGroupRequestDTO request
    ) {

        ApiResponse<GroupResponseDTO> response = groupService.updateGroup(groupId, request);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<ApiResponse<Void>> deleteGroup(
            @PathVariable Long groupId) {


        ApiResponse<Void> response = groupService.deleteGroup(groupId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }
}