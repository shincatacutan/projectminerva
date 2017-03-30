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

@Entity
@Table(name = "document")
public class Document {
	public Document() {
	}

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "filename_", nullable = false)
	private String filename;

	@ManyToOne(optional = false)
	@JoinColumn(name = "category_")
	private Category category;

	@ManyToOne(optional = true)
	@JoinColumn(name = "sub_category")
	private Category subCategory;

	@ManyToOne(optional = false)
	@JoinColumn(name = "line_of_business")
	private LineOfBusiness lineOfBusiness;

	@Column(name = "detailed_info", nullable = false)
	private String detailedInfo;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "tags", nullable = true)
	private String tags;

	@Column(name = "created_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime createDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "created_by")
	private UserInfo createUser;

	@Column(name = "updated_date", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
	private LocalDateTime updateDate;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "updated_by")
	private UserInfo updateUser;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Category getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(Category subCategory) {
		this.subCategory = subCategory;
	}

	public String getDetailedInfo() {
		return detailedInfo;
	}

	public void setDetailedInfo(String detailedInfo) {
		this.detailedInfo = detailedInfo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public UserInfo getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(UserInfo updateUser) {
		this.updateUser = updateUser;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public LineOfBusiness getLineOfBusiness() {
		return lineOfBusiness;
	}

	public void setLineOfBusiness(LineOfBusiness lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}

}
