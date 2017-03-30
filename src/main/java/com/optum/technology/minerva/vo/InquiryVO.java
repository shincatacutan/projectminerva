package com.optum.technology.minerva.vo;

public class InquiryVO {

	private String id;

	private String title;
	private String body;
	private String createDate;
	private String createUser;
	private String status;
	private String statusString;
	private String enabled;
	private String lob;
	private String assignedSME;
	
	private String path;

	// private List<InquiryReply> reply;
	public InquiryVO(String id, String title, String body, String createDate,
			String createUser, String status, String statusString, String enabled, String lob, String path, String assignedSME) {
		super();
		this.id = id;
		this.title = title;
		this.body = body;
		this.createDate = createDate;
		this.createUser = createUser;
		this.status = status;
		this.statusString = statusString;
		this.enabled = enabled;
		this.lob = lob;
		this.path = path;
		this.assignedSME = assignedSME;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getAssignedSME() {
		return assignedSME;
	}

	public void setAssignedSME(String assignedSME) {
		this.assignedSME = assignedSME;
	}

}
