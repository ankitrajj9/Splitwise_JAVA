package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Long>{
	@Query("SELECT message FROM Message message WHERE message.partyA.id=:partyAId OR message.partyB.id=:partyAId ORDER BY message.updatedOn DESC ")
	List<Message> getMessageForList (@Param("partyAId") Long partyAId) ;
	
	@Query("SELECT message FROM Message message WHERE (message.partyA.id=:partyAId AND message.partyB.id=:partyBId) OR (message.partyA.id=:partyBId AND message.partyB.id=:partyAId) ")
	Message getMessageBetweenUser (@Param("partyAId") Long partyAId,@Param("partyBId") Long partyBId) ;
	
	@Query("SELECT distinct userImage FROM Message message INNER JOIN UserImage userImage ON userImage.user.id=message.partyA.id OR message.partyB.id=userImage.user.id WHERE message.partyA.id=:partyAId OR message.partyB.id=:partyAId ")
	List<UserImage> getMessageUserImage (@Param("partyAId") Long partyAId) ;
	
	@Query("SELECT message FROM Message message WHERE message.partyA.id=:partyAId OR message.partyB.id=:partyAId ")
	List<Message> getMessageUserList (@Param("partyAId") Long partyAId) ;

}
