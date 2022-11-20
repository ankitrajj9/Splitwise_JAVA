package com.ankit.angularapp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserExpenseRepository extends JpaRepository<UserExpense, Long> {
	@Query("select userExpense from UserExpense userExpense where userExpense.createdBy.id=:createdBy and userExpense.userId.id=:userId and userExpense.group.groupId=:groupId ")
	List<UserExpense> paidByMeWithThem(@Param("createdBy") Long createdBy,@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query("select userExpense from UserExpense userExpense where userExpense.createdBy.id=:createdBy and userExpense.userId.id=:userId and userExpense.group.groupId=:groupId ")
	List<UserExpense> paidByThemWithMe(@Param("createdBy") Long createdBy,@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query("select userExpense from UserExpense userExpense where userExpense.group.groupId=:groupId and userExpense.createdBy.id=:userId and userExpense.cstatus=0 and userExpense.userId.id<>:userId")
	List<UserExpense> getTotalLendedAmt(@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query("select userExpense from UserExpense userExpense where userExpense.group.groupId=:groupId and userExpense.userId.id=:userId  and userExpense.cstatus=0 and userExpense.createdBy.id<>:userId")
	List<UserExpense> getTotalBorrowedAmt(@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query(" select SUM(userExpense.amount) from UserExpense userExpense where userExpense.createdBy.id=:userId and userExpense.group.groupId=:groupId ")
	Double getTotalExpenseByMe(@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query(" select userExpense from UserExpense userExpense where userExpense.expense.expenseId=:expenseId and userExpense.userId.id=:userId ")
	List<UserExpense> partOfExpense(@Param("expenseId") Long expenseId,@Param("userId") Long userId);
	
	@Query("select SUM(userExpense.expense.amount) from UserExpense userExpense where userExpense.createdBy.id=:createdBy and userExpense.userId.id=:userId and userExpense.group.groupId=:groupId and userExpense.cstatus=1 ")
	BigDecimal spentByMeWithThemAndReturnedByThem(@Param("createdBy") Long createdBy,@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query("select SUM(userExpense.expense.amount) from UserExpense userExpense where userExpense.createdBy.id=:createdBy and userExpense.userId.id=:userId and userExpense.group.groupId=:groupId and userExpense.cstatus=1 ")
	BigDecimal spentByThemWithMeAndReturnedByMe(@Param("createdBy") Long createdBy,@Param("userId") Long userId,@Param("groupId") Long groupId);
	
	@Query("select userExpense from UserExpense userExpense where (userExpense.createdBy.id=:toId and userExpense.userId.id=:fromId and userExpense.group.groupId=:groupId and userExpense.cstatus=0) or (userExpense.userId.id=:toId and userExpense.createdBy.id=:fromId and userExpense.group.groupId=:groupId and userExpense.cstatus=0) and userExpense.cstatus=0 ")
	List<UserExpense> getSettleUpDetails(@Param("groupId") Long groupId,@Param("fromId") Long userId,@Param("toId") Long toId);
	
	@Query(" select userExpense.createdBy.name,SUM(userExpense.amount) from UserExpense userExpense where userExpense.group.groupId=:groupId GROUP BY userExpense.createdBy.id ")
	List<Object[]> getGroupByExpenseOfUser(@Param("groupId") Long groupId);
	
	@Query("select userExpense from UserExpense userExpense where userExpense.userId.id=:userId and userExpense.group.groupId=:groupId ")
	List<UserExpense> getShareDetails(@Param("groupId") Long groupId,@Param("userId") Long userId);
	
	@Query("select userExpense from UserExpense userExpense where userExpense.createdBy.id=:userId and userExpense.cstatus=0 and userExpense.userId.id<>:userId")
	List<UserExpense> getOverAllLendedAmt(@Param("userId") Long userId);
	
	@Query("select userExpense from UserExpense userExpense where  userExpense.userId.id=:userId  and userExpense.cstatus=0 and userExpense.createdBy.id<>:userId")
	List<UserExpense> getOverAllBorrowedAmt(@Param("userId") Long userId);
}
