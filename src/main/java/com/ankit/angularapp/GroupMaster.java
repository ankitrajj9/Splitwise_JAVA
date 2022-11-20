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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="groupmaster")
public class GroupMaster {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long groupId;
	
	private String groupName;
	private Date createdOn;
	private BigDecimal totalExpense;
	private int totalUsers;
	private Long createdBy;
	
	@JsonIgnore
	@OneToMany(fetch=FetchType.EAGER,cascade=CascadeType.ALL,mappedBy="groupMaster")
	private Set<GroupUser> groupUsers = new HashSet<GroupUser>();
	
	

	public Long getCreatedBy() {
		return createdBy;
	}



	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}



	public Set<GroupUser> getGroupUsers() {
		return groupUsers;
	}



	public void setGroupUsers(Set<GroupUser> groupUsers) {
		this.groupUsers = groupUsers;
	}



	public Long getGroupId() {
		return groupId;
	}



	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}



	public String getGroupName() {
		return groupName;
	}



	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public Date getCreatedOn() {
		return createdOn;
	}



	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



	public BigDecimal getTotalExpense() {
		return totalExpense;
	}



	public void setTotalExpense(BigDecimal totalExpense) {
		this.totalExpense = totalExpense;
	}



	public int getTotalUsers() {
		return totalUsers;
	}



	public void setTotalUsers(int totalUsers) {
		this.totalUsers = totalUsers;
	}
	public GroupMaster(Long groupId) {
		this.groupId=groupId;
	}


	public GroupMaster() {}
	  
}
