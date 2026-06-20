package com.splitManager.splitmanager.controller;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupMemberResponseDTO;
import com.splitManager.splitmanager.security.JwtUtil;
import com.splitManager.splitmanager.service.GroupMemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/{groupId}/members")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupMemberService groupMemberService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ApiResponse<GroupMemberResponseDTO>> addMember(
            @PathVariable Long groupId,
            @RequestParam Long userId

    ) {

        ApiResponse<GroupMemberResponseDTO> response =
                groupMemberService.addMember(groupId, userId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeMember(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            HttpServletRequest httpServletRequest
    ) {

        String email = jwtUtil.extractEmailFromRequest(httpServletRequest);

        ApiResponse<Void> response =
                groupMemberService.removeMember(groupId, userId, email);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GroupMemberResponseDTO>>> getMembers(
            @PathVariable Long groupId
    ) {

        ApiResponse<List<GroupMemberResponseDTO>> response =
                groupMemberService.getMembers(groupId);

        return ResponseEntity.status(response.getStatus())
                .body(response);
    }
}