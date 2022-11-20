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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="userFollower")
public class UserFollower {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userFollwerId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="userId")
	private User userId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="follwerId")
	private User follwerId;
	private Date createdOn;
	public UserFollower() {}
	public Long getUserFollwerId() {
		return userFollwerId;
	}
	public void setUserFollwerId(Long userFollwerId) {
		this.userFollwerId = userFollwerId;
	}
	public User getUserId() {
		return userId;
	}
	public void setUserId(User userId) {
		this.userId = userId;
	}
	public User getFollwerId() {
		return follwerId;
	}
	public void setFollwerId(User follwerId) {
		this.follwerId = follwerId;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	

}
