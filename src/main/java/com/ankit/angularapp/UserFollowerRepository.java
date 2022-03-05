package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFollowerRepository extends JpaRepository<UserFollower, Long>{
	@Query("SELECT uf FROM UserFollower uf WHERE uf.follwerId.id = :followerId and uf.userId.id = :userId")
	UserFollower userFollows(@Param("followerId") Long followerId,@Param("userId") Long userId);
	
	@Query("SELECT uf.userId FROM UserFollower uf WHERE uf.follwerId.id = :followerId ")
	List<User> getFollowingUser(@Param("followerId") Long followerId);
	
	@Query("SELECT uf.follwerId FROM UserFollower uf WHERE uf.userId.id = :userId ")
	List<User> getFollowerUser(@Param("userId") Long userId);
}
