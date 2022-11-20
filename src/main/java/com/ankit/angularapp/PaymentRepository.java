package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	@Query(" select payment from Payment payment where payment.toId.id=:toId and payment.groupMaster.groupId=:groupId and payment.cstatus=0 ")
	List<Payment> getSettleUpRequest(@Param("toId") Long toId,@Param("groupId") Long groupId);
	
	@Query(" select payment from Payment payment where payment.toId.id=:toId and payment.fromId.id=:fromId and payment.groupMaster.groupId=:groupId and payment.cstatus=0 ")
	Payment isSettleUpReqInitiated(@Param("toId") Long toId,@Param("fromId") Long fromId,@Param("groupId") Long groupId);
	
	@Query(" select payment from Payment payment where payment.groupMaster.groupId=:groupId and payment.cstatus in (1,2) ")
	List<Payment> getGroupPaymentDtls(@Param("groupId") Long groupId);
}
