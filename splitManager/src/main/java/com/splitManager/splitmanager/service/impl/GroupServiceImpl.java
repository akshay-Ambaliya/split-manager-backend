package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.request.CreateGroupRequestDTO;
import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupResponseDTO;
import com.splitManager.splitmanager.entity.Group;
import com.splitManager.splitmanager.entity.GroupMember;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.GroupMemberRepository;
import com.splitManager.splitmanager.repository.GroupRepository;
import com.splitManager.splitmanager.repository.UserRepository;
import com.splitManager.splitmanager.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public ApiResponse<GroupResponseDTO> createGroup(CreateGroupRequestDTO request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Group group = Group.builder()
                .groupName(request.getGroupName())
                .description(request.getDescription())
                .createdBy(user)
                .build();

        group = groupRepository.save(group);

        groupMemberRepository.save(GroupMember
                        .builder()
                        .user(user)
                        .group(group)
                        .build()
        );

        return new ApiResponse<GroupResponseDTO>(HttpStatus.OK, "Group created successfully", mapToDTO(group));
    }

    @Override
    public ApiResponse<List<GroupResponseDTO>> getMyGroups(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return new ApiResponse<>(HttpStatus.OK, "Groups fetched successfully",groupRepository.findByCreatedById(user.getId())
                .stream()
                .map(this::mapToDTO)
                .toList());
    }

    @Override
    public ApiResponse<GroupResponseDTO> getGroupById(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        return new ApiResponse<>(HttpStatus.OK, "Group fetched successfully", mapToDTO(group));
    }

    @Override
    public ApiResponse<GroupResponseDTO> updateGroup(Long groupId, CreateGroupRequestDTO request) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        group.setGroupName(request.getGroupName());
        group.setDescription(request.getDescription());

        groupRepository.save(group);

        return new ApiResponse<>(HttpStatus.OK, "Group updated successfully", mapToDTO(group));
    }

    @Override
    public ApiResponse<Void> deleteGroup(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        groupRepository.delete(group);

        return new ApiResponse<>(HttpStatus.NO_CONTENT, "Group deleted successfully", null);
    }

    private GroupResponseDTO mapToDTO(Group group) {

        return GroupResponseDTO.builder()
                .groupId(group.getId())
                .groupName(group.getGroupName())
                .description(group.getDescription())
                .createdBy(group.getCreatedBy().getId())
                .build();
    }
}