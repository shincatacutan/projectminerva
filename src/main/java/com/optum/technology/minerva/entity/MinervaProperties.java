package com.optum.technology.minerva.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mom_properties")
public class MinervaProperties {


	@Id
	@Column(name = "id", unique = true, nullable = false, length = 45)
	private String id;

	@Column(name = "value", nullable = false)
	private String value;

	public MinervaProperties() {
		super();
	}
	
	public MinervaProperties(String id, String value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}