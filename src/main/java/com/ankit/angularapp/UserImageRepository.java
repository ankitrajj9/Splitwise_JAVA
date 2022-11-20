package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserImageRepository extends JpaRepository<UserImage, Long>{
	@Query(" select userImage from UserImage userImage where userImage.user.id=:userId ")
	UserImage getUserImage(@Param("userId") Long userId);
	
	@Query(value = "SELECT ui.* FROM UserImage ui inner join groupUser gu on gu.id=ui.userId inner join GroupMaster gm on gm.groupId=gu.groupId where gm.groupId=:groupId ",
            nativeQuery=true)
	List<UserImage> getGroupUserImage(@Param("groupId") Long groupId);
}
