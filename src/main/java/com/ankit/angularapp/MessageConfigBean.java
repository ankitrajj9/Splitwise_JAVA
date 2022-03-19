package com.ankit.angularapp;

import java.io.Serializable;

public class MessageConfigBean implements Serializable {
	public static final long serialVersionUID = 1L;
	private int recipientId;
	private String recipientName;
	private String fromMailId;
	private String mailSubject;
	private String mailContent;
	
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public String getMailContent() {
		return mailContent;
	}
	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}
	public String getFromMailId() {
		return fromMailId;
	}
	public void setFromMailId(String fromMailId) {
		this.fromMailId = fromMailId;
	}
	private String toMailId;
	public int getRecipientId() {
		return recipientId;
	}
	public void setRecipientId(int recipientId) {
		this.recipientId = recipientId;
	}
	public String getRecipientName() {
		return recipientName;
	}
	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}
	public String getToMailId() {
		return toMailId;
	}
	public void setToMailId(String toMailId) {
		this.toMailId = toMailId;
	}
	
}
