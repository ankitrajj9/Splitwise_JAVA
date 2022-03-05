package com.ankit.angularapp;

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
@Table(name="groupuser")
public class GroupUser {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long groupUserId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id")
	private User user;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="groupId")
	private GroupMaster groupMaster;
	
	private Date addedOn;
	
	public GroupUser() {}
	public Long getGroupUserId() {
		return groupUserId;
	}
	public void setGroupUserId(Long groupUserId) {
		this.groupUserId = groupUserId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	public GroupMaster getGroupMaster() {
		return groupMaster;
	}
	public void setGroupMaster(GroupMaster groupMaster) {
		this.groupMaster = groupMaster;
	}
	public Date getAddedOn() {
		return addedOn;
	}
	public void setAddedOn(Date addedOn) {
		this.addedOn = addedOn;
	}
	
	
}
