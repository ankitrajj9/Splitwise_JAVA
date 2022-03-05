package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupUserRepository extends JpaRepository<GroupUser, Long>{
	@Query("select groupUser FROM GroupUser groupUser WHERE groupUser.groupMaster.groupId=:groupId")
	List<GroupUser> getGroupUsers(@Param("groupId")Long groupId);
	
	@Query("select groupUser.user.name from GroupUser groupUser where groupUser.groupMaster.groupId=:groupId")
	List<String> getGroupUserNames(@Param("groupId") Long groupId);
	
	@Query("select groupUser.user from GroupUser groupUser where groupUser.groupMaster.groupId=:groupId")
	List<User> getGroupUsersByGroupId(@Param("groupId") Long groupId);
	
	@Query("select distinct (groupUser.groupMaster) from GroupUser groupUser where groupUser.user.id in :userIds")
	List<GroupMaster> getMutualGroups(@Param("userIds") Long[] userIds);
	
	@Query("select groupUser from GroupUser groupUser where groupUser.groupMaster.groupId=:groupId and groupUser.user.id=:userId")
	GroupUser isPartOfGroup(@Param("groupId") Long groupId,@Param("userId") Long userId);
}
