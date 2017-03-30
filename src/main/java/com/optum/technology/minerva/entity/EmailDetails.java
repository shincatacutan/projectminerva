package com.optum.technology.minerva.entity;

public class EmailDetails {

	private String senderEmail;
	private String senderName;
	private String recipient;
	private String htmlBody;
	private String subject;
	private String appName;

	public String getEmailSender() {
		return senderEmail;
	}
	public void setEmailSender(String emailSender) {
		this.senderEmail = emailSender;
	}
	public String getEmailIdentity() {
		return senderName;
	}
	public void setEmailIdentity(String emailIdentity) {
		this.senderName = emailIdentity;
	}
	
	public String getHtmlBody() {
		return htmlBody;
	}
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}

}
