package com.ankit.angularapp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="expense")
public class Expense {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long expenseId;
	private String description;
	private BigDecimal amount;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	private GroupMaster groupMaster;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id")
	private User user;
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL,mappedBy="expense")
	private Set<UserExpense> userExpenses = new HashSet<UserExpense>();
	private Date createdOn;
	public Expense() {}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(Long expenseId) {
		this.expenseId = expenseId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public GroupMaster getGroupMaster() {
		return groupMaster;
	}

	public void setGroupMaster(GroupMaster groupMaster) {
		this.groupMaster = groupMaster;
	}

	public Set<UserExpense> getUserExpenses() {
		return userExpenses;
	}
	public void setUserExpenses(Set<UserExpense> userExpenses) {
		this.userExpenses = userExpenses;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	

}
