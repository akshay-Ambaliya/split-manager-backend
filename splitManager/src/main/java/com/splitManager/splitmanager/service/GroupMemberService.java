package com.splitManager.splitmanager.service;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupMemberResponseDTO;

import java.util.List;

public interface GroupMemberService {

    ApiResponse<GroupMemberResponseDTO> addMember(Long groupId, Long userId);

    ApiResponse<Void> removeMember(Long groupId, Long userId, String email);

    ApiResponse<List<GroupMemberResponseDTO>> getMembers(Long groupId);
}