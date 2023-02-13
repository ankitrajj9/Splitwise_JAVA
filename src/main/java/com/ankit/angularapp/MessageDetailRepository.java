package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface MessageDetailRepository extends JpaRepository<MessageDetail, Long>,PagingAndSortingRepository<MessageDetail, Long>{
	@Query("SELECT messageDetail FROM MessageDetail messageDetail WHERE (messageDetail.fromId.id=:userId AND messageDetail.toId.id=:recipientId) OR (messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId) ORDER BY messageDetail.createdOn ")
	List<MessageDetail> getMessages (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
	
	@Query("SELECT COUNT(messageDetail.messageDetailId) FROM MessageDetail messageDetail WHERE (messageDetail.fromId.id=:userId AND messageDetail.toId.id=:recipientId) OR (messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId) ORDER BY messageDetail.createdOn ")
	Long getMessagesCount (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
	
	@Query("SELECT COUNT(messageDetail.messageDetailId) FROM MessageDetail messageDetail WHERE messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId AND messageDetail.isRead=0 ")
	Long getUserWiseUnreadCount (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
	
	@Query("SELECT MAX(messageDetail.messageDetailId) FROM MessageDetail messageDetail ")
	Long getMaxMessagesDtlId () ;
	
	@Query(" SELECT count(messageDetail.toId.id) FROM MessageDetail messageDetail WHERE messageDetail.toId.id=:toId AND messageDetail.isRead=0 ")
	Long getUnreadMessageByUserCount(@Param("toId") Long toId);
	
	@Query(" SELECT messageDetail FROM MessageDetail messageDetail WHERE messageDetail.toId.id=:toId AND messageDetail.isRead=0 AND messageDetail.fromId.id=:fromId ")
	List<MessageDetail> getUnReadMessageByUser(@Param("toId") Long toId,@Param("fromId") Long fromId);
	
	@Query(" SELECT distinct(messageDetail.fromId) FROM MessageDetail messageDetail WHERE messageDetail.toId.id=:toId AND messageDetail.isRead=0 ")
	List<User> getUnReadMessageUserList(@Param("toId") Long toId);
	
	@Query("SELECT messageDetail FROM MessageDetail messageDetail WHERE (messageDetail.fromId.id=:userId AND messageDetail.toId.id=:recipientId) OR (messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId) ORDER BY messageDetail.createdOn ")
	List<MessageDetail> getLimitedMessages (@Param("userId") Long userId,@Param("recipientId") Long recipientId,Pageable pageable) ;
	
	@Query(nativeQuery = true,value="(SELECT * FROM MessageDetail messageDetail WHERE ((messageDetail.fromId=:userId AND messageDetail.toId=:recipientId) OR (messageDetail.fromId=:recipientId AND messageDetail.toId=:userId)) ORDER BY messageDetail.messageDetailId desc LIMIT 10) ORDER BY messageDetailId ASC ")
	List<MessageDetail> getTop10Messages (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
	
	@Query(nativeQuery = true,value="(SELECT * FROM MessageDetail messageDetail WHERE ((messageDetail.fromId=:userId AND messageDetail.toId=:recipientId) OR (messageDetail.toId=:userId AND messageDetail.fromId=:recipientId)) AND messageDetail.messageDetailId<:lastMaxId ORDER BY messageDetail.messageDetailId DESC LIMIT 10) ORDER BY messageDetailId ASC ")
	List<MessageDetail> getNext10Messages (@Param("userId") Long userId,@Param("recipientId") Long recipientId,@Param("lastMaxId") Long lastMaxId) ;

	@Query("SELECT max (messageDetail.messageDetailId) FROM MessageDetail messageDetail WHERE (messageDetail.fromId.id=:userId AND messageDetail.toId.id=:recipientId) OR (messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId) ")
	Long getMaxMessagedetailId (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
	
	@Query("SELECT min (messageDetail.messageDetailId) FROM MessageDetail messageDetail WHERE (messageDetail.fromId.id=:userId AND messageDetail.toId.id=:recipientId) OR (messageDetail.fromId.id=:recipientId AND messageDetail.toId.id=:userId) ")
	Long getInitialMessagesDtlId (@Param("userId") Long userId,@Param("recipientId") Long recipientId) ;
}
