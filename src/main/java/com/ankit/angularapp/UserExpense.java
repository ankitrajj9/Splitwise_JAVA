package com.ankit.angularapp;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="userexpense")
public class UserExpense {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userExpenseId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="expenseId")
	private Expense expense;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="createdBy")
	private User createdBy;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User userId;
	private BigDecimal amount;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	private GroupMaster group;
	
	private Date createdOn;
	
	private int cstatus;
	
	public UserExpense() {}
	public Long getUserExpenseId() {
		return userExpenseId;
	}
	public void setUserExpenseId(Long userExpenseId) {
		this.userExpenseId = userExpenseId;
	}
	public Expense getExpense() {
		return expense;
	}
	public void setExpense(Expense expense) {
		this.expense = expense;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public GroupMaster getGroup() {
		return group;
	}
	public void setGroup(GroupMaster group) {
		this.group = group;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public int getCstatus() {
		return cstatus;
	}
	public void setCstatus(int cstatus) {
		this.cstatus = cstatus;
	}
	
	
	
}
