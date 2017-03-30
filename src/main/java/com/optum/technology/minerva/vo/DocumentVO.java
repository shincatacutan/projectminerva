package com.optum.technology.minerva.vo;

public class DocumentVO {
	private int docId;
	private int catId;
	private String catName;
	private String fileName;
	private String createDate;
	private String createUser;
	private String detailedInfo;
	private int subCatId;
	private String subCatName;
	private String tags;
	private String title;
	private String updateDate;
	private String updateUser;
	private String lob;
	private boolean isActive;

	
	public DocumentVO(int docId, int catId, String catName, String fileName,
			String createDate, String createUser, String detailedInfo,
			int subCatId, String subCatName, String tags, String title,
			String updateDate, String updateUser, String lob, boolean isActive) {
		super();
		this.docId = docId;
		this.catId = catId;
		this.catName = catName;
		this.fileName = fileName;
		this.createDate = createDate;
		this.createUser = createUser;
		this.detailedInfo = detailedInfo;
		this.subCatId = subCatId;
		this.subCatName = subCatName;
		this.tags = tags;
		this.title = title;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
		this.setLob(lob);
		this.isActive = isActive;
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
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
	public String getDetailedInfo() {
		return detailedInfo;
	}
	public void setDetailedInfo(String detailedInfo) {
		this.detailedInfo = detailedInfo;
	}
	public int getSubCatId() {
		return subCatId;
	}
	public void setSubCatId(int subCatId) {
		this.subCatId = subCatId;
	}
	public String getSubCatName() {
		return subCatName;
	}
	public void setSubCatName(String subCatName) {
		this.subCatName = subCatName;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getLob() {
		return lob;
	}
	public void setLob(String lob) {
		this.lob = lob;
	}
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

}
