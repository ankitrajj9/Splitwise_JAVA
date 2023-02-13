package com.ankit.angularapp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="messagedetail")
public class MessageDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long messageDetailId;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="fromId")
	private User fromId;
	
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="toId")
	private User toId;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd/MM/yyyy HH:mm:ss")
	@CreationTimestamp
	private Date createdOn;
	
	private int contentType;
	
	//1 for String 2 for media
	private String content;
	
	@Lob
	private byte[] image;
	
	private int isRead;
	
	
	
	public int getIsRead() {
		return isRead;
	}



	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}



	public MessageDetail() {}
	
	

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



	public Long getMessageDetailId() {
		return messageDetailId;
	}

	public void setMessageDetailId(Long messageDetailId) {
		this.messageDetailId = messageDetailId;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}



	public byte[] getImage() {
		return image;
	}



	public void setImage(byte[] image) {
		this.image = image;
	}
	
	
	

}
