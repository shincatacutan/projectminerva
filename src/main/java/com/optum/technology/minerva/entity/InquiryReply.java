package com.optum.technology.minerva.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "inquiry_reply")
public class InquiryReply {
	public InquiryReply() {
	}
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "reply_body", nullable = false)
	private String replyBody;

	@Column(name = "created_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime createDate;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE ,optional=false)
	@JoinColumn(name = "create_user")
	private UserInfo createUser;
	
	@ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
	@JoinColumn(name = "inquiry_id", nullable = false)
	@JsonManagedReference
	private Inquiry inquiryId;
	
	@Column(name = "path", nullable = true)
	private String path;
	
	@Column(name = "filename_", nullable = true)
	private String filename;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getReplyBody() {
		return replyBody;
	}
	public void setReplyBody(String replyBody) {
		this.replyBody = replyBody;
	}
	
	public UserInfo getCreateUser() {
		return createUser;
	}
	public void setCreateUser(UserInfo createUser) {
		this.createUser = createUser;
	}
	
	public LocalDateTime getCreateDate() {
		return createDate;
	}
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Inquiry getInquiryId() {
		return inquiryId;
	}
	public void setInquiryId(Inquiry inquiryId) {
		this.inquiryId = inquiryId;
	}

}
