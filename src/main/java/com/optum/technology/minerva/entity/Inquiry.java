package com.optum.technology.minerva.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "inquiry")
public class Inquiry {

	public Inquiry() {
	}

	@Id
	@Column(name = "inq_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "body", nullable = false)
	private String body;

	@Column(name = "created_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime createDate;

	@ManyToOne(optional = false)
	@JoinColumn(name = "line_of_business")
	private LineOfBusiness lineOfBusiness;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "created_by")
	private UserInfo createUser;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "assigned_sme")
	private UserInfo assignedSME;
	
	@JsonBackReference
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "inquiryId", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<InquiryReply> replies;

	@Column(name = "status", nullable = false)
	private byte status;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@Column(name = "path", nullable = true)
	private String path;

	@Column(name = "filename_", nullable = true)
	private String filename;

	@Column(name = "updated_date", nullable = true)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime updateDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "updated_by")
	private UserInfo updateUser;

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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public UserInfo getCreateUser() {
		return createUser;
	}

	public void setCreateUser(UserInfo createUser) {
		this.createUser = createUser;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public LineOfBusiness getLineOfBusiness() {
		return lineOfBusiness;
	}

	public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Inquiry other = (Inquiry) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public UserInfo getAssignedSME() {
		return assignedSME;
	}

	public void setAssignedSME(UserInfo assignedSME) {
		this.assignedSME = assignedSME;
	}

	public UserInfo getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(UserInfo updateUser) {
		this.updateUser = updateUser;
	}

	public List<InquiryReply> getReplies() {
		return replies;
	}

	public void setReplies(List<InquiryReply> replies) {
		this.replies = replies;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

}
