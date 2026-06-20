package com.splitManager.splitmanager.service.impl;

import com.splitManager.splitmanager.dto.response.ApiResponse;
import com.splitManager.splitmanager.dto.response.GroupMemberResponseDTO;
import com.splitManager.splitmanager.entity.Group;
import com.splitManager.splitmanager.entity.GroupMember;
import com.splitManager.splitmanager.entity.User;
import com.splitManager.splitmanager.exception.BadRequestException;
import com.splitManager.splitmanager.exception.BusinessException;
import com.splitManager.splitmanager.exception.ResourceNotFoundException;
import com.splitManager.splitmanager.repository.GroupMemberRepository;
import com.splitManager.splitmanager.repository.GroupRepository;
import com.splitManager.splitmanager.repository.UserRepository;
import com.splitManager.splitmanager.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public ApiResponse<GroupMemberResponseDTO> addMember(Long groupId, Long userId) {


        log.error("Inside addMember function");

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (groupMemberRepository.existsByGroupAndUser(group, user)) {
            throw new BadRequestException("User already exists in group");
        }

        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        groupMemberRepository.save(member);

        GroupMemberResponseDTO dto = mapToDTO(member);

        return ApiResponse.<GroupMemberResponseDTO>builder()
                .success(true)
                .message("Member added successfully")
                .status(HttpStatus.CREATED)
                .data(dto)
                .build();
    }

    @Override
    public ApiResponse<Void> removeMember(Long groupId, Long userId, String email) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember member = groupMemberRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        groupMemberRepository.delete(member);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Member removed successfully")
                .status(HttpStatus.OK)
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<List<GroupMemberResponseDTO>> getMembers(Long groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found"));

        List<GroupMemberResponseDTO> members = groupMemberRepository.findByGroup(group)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return ApiResponse.<List<GroupMemberResponseDTO>>builder()
                .success(true)
                .message("Group members fetched successfully")
                .status(HttpStatus.OK)
                .data(members)
                .build();
    }

    private GroupMemberResponseDTO mapToDTO(GroupMember member) {
        return GroupMemberResponseDTO.builder()
                .userId(member.getUser().getId())
                .fullName(member.getUser().getFullName())
                .email(member.getUser().getEmail())
                .build();
    }
}