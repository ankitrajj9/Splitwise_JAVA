package com.ankit.angularapp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{
	@Query("select expense from Expense expense where expense.groupMaster.groupId=:groupId ")
	List<Expense> totalExpenseByMeWIthGroup(@Param("groupId") Long groupId);
}
