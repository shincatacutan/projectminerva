package com.optum.technology.minerva.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login_tries")
public class LoginTries {
	
	@Id
	@Column(name = "username", unique = true, nullable = false, length = 45)
	private String username;

	@Column(name = "tries", nullable = false)
	private int tries;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getTries() {
		return tries;
	}
	public void setTries(int tries) {
		this.tries = tries;
	}
	
	public LoginTries(String username, int tries) {
		super();
		this.username = username;
		this.tries = tries;
	}
	public LoginTries() {
		super();
	}

}
