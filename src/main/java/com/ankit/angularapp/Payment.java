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
@Table(name="payment")
public class Payment {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long paymentId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fromId")
	private User fromId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="toId")
	private User toId;
	private Date createdOn;
	private BigDecimal amount;
	private int cstatus;
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}





	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}





	public GroupMaster getGroupMaster() {
		return groupMaster;
	}





	public void setGroupMaster(GroupMaster groupMaster) {
		this.groupMaster = groupMaster;
	}





	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	private GroupMaster groupMaster;
	
	
	
	
	
	public Long getPaymentId() {
		return paymentId;
	}





	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}





	public User getFromId() {
		return fromId;
	}





	public void setFromId(User fromId) {
		this.fromId = fromId;
	}





	public User getToId() {
		return toId;
	}





	public void setToId(User toId) {
		this.toId = toId;
	}





	public Date getCreatedOn() {
		return createdOn;
	}





	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}





	public BigDecimal getAmount() {
		return amount;
	}





	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}





	public int getCstatus() {
		return cstatus;
	}





	public void setCstatus(int cstatus) {
		this.cstatus = cstatus;
	}





	public GroupMaster getGroupId() {
		return groupMaster;
	}





	public void setGroupId(GroupMaster groupMaster) {
		this.groupMaster = groupMaster;
	}





	public Payment() {}

}
