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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="message")
public class Message {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long messageId;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="partyA")
	private User partyA;
	
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="partyB")
	private User partyB;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd/MM/yyyy")
	@CreationTimestamp
	private Date createdOn;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd/MM/yyyy")
	@UpdateTimestamp
	private Date updatedOn;
	
	public Message() {}

	public Long getMessageId() {
		return messageId;
	}

	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}

	public User getPartyA() {
		return partyA;
	}

	public void setPartyA(User partyA) {
		this.partyA = partyA;
	}

	public User getPartyB() {
		return partyB;
	}

	public void setPartyB(User partyB) {
		this.partyB = partyB;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	

}
