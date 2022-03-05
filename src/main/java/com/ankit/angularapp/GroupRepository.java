package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<GroupMaster, Long> {
	@Query("SELECT groupUser.groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user WHERE user.id = :id")
	List<GroupMaster> findUserGroups(@Param("id") Long id);
	@Query("SELECT group FROM GroupMaster group WHERE group.groupId = :groupId")
	GroupMaster getGroupById(@Param("groupId") Long groupId);
}
