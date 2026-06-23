package com.splitManager.splitmanager.repository;

import com.splitManager.splitmanager.entity.GroupMember;
import com.splitManager.splitmanager.entity.Group;
import com.splitManager.splitmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    boolean existsByGroupAndUser(Group group, User user);

    List<GroupMember> findByGroup(Group group);

    Optional<GroupMember> findByGroupAndUser(Group group, User user);

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);
}