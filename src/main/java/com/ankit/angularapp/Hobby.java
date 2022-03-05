package com.ankit.angularapp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name="hobby")
public class Hobby {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="hobbyId")
	@JsonIgnore
	private Long hobbyId;
	
	private String hobbyName;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id")
	private User user;
	public Hobby() {}
	
	public Hobby(Long hobbyId, String hobbyName, User user) {
		super();
		this.hobbyId = hobbyId;
		this.hobbyName = hobbyName;
		this.user = user;
	}

	public Long getHobbyId() {
		return hobbyId;
	}
	public void setHobbyId(Long hobbyId) {
		this.hobbyId = hobbyId;
	}
	public String getHobbyName() {
		return hobbyName;
	}
	public void setHobbyName(String hobbyName) {
		this.hobbyName = hobbyName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
