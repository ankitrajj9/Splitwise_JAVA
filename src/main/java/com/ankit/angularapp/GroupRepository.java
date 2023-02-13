package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<GroupMaster, Long> {
	@Query("SELECT groupUser.groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user WHERE user.id = :id order by groupUser.groupMaster.createdOn DESC ")
	List<GroupMaster> findUserGroups(@Param("id") Long id);
	@Query("SELECT group FROM GroupMaster group WHERE group.groupId = :groupId")
	GroupMaster getGroupById(@Param("groupId") Long groupId);
	@Query("SELECT groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user INNER JOIN groupUser.groupMaster groupMaster WHERE user.id = :id and groupMaster.groupName like %:searchText% order by groupMaster.groupName ASC ")
	List<GroupMaster> searchUserGroups(@Param("id") Long id,@Param("searchText") String searchText);
	@Query("SELECT groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user INNER JOIN groupUser.groupMaster groupMaster WHERE user.id = :id and groupMaster.groupName like %:searchText% order by groupMaster.groupName ASC ")
	List<GroupMaster> searchSortUserGroupsAZ(@Param("id") Long id,@Param("searchText") String searchText);
	@Query("SELECT groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user INNER JOIN groupUser.groupMaster groupMaster WHERE user.id = :id and groupMaster.groupName like %:searchText% order by groupMaster.groupName DESC ")
	List<GroupMaster> searchSortUserGroupsZA(@Param("id") Long id,@Param("searchText") String searchText);
	@Query("SELECT groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user INNER JOIN groupUser.groupMaster groupMaster WHERE user.id = :id and groupMaster.groupName like %:searchText% order by groupMaster.createdOn ASC ")
	List<GroupMaster> searchSortUserGroupsCreatedOnASC(@Param("id") Long id,@Param("searchText") String searchText);
	@Query("SELECT groupMaster FROM GroupUser groupUser INNER JOIN groupUser.user user INNER JOIN groupUser.groupMaster groupMaster WHERE user.id = :id and groupMaster.groupName like %:searchText% order by groupMaster.createdOn DESC ")
	List<GroupMaster> searchSortUserGroupsCreatedOnDESC(@Param("id") Long id,@Param("searchText") String searchText);
}
