package com.splitManager.splitmanager.service;

import com.splitManager.splitmanager.dto.request.CreateGroupRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupResponseDTO;
import com.splitManager.splitmanager.entity.User;

import java.util.List;

public interface GroupService {

    ApiResponse<GroupResponseDTO> createGroup(CreateGroupRequestDTO request, String user);

    ApiResponse<List<GroupResponseDTO>> getMyGroups(String email);

    ApiResponse<GroupResponseDTO> getGroupById(Long groupId);

    ApiResponse<GroupResponseDTO> updateGroup(Long groupId, CreateGroupRequestDTO request);

    ApiResponse<Void> deleteGroup(Long groupId);
}