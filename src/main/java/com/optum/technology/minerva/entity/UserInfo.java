package com.optum.technology.minerva.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "users")
public class UserInfo {

	private String username;
	private String empid;
	private String lastName;
	private String firstName;
	private String email;
	private byte[] password;
	private byte[] salt;
	private boolean enabled;
	private Set<UserRole> userRole = new HashSet<UserRole>(0);

	public UserInfo() {
	}

	public UserInfo(String username, String empid, String lastName, String firstName, String email, byte[] password,
			boolean enabled, Set<UserRole> userRole) {
		super();
		this.username = username;
		this.empid = empid;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.userRole = userRole;
	}

	public UserInfo(String username, String empid, String lastName, String firstName, String email, byte[] password,
			boolean enabled) {
		super();
		this.username = username;
		this.empid = empid;
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
	}

	@Id
	@Column(name = "username", unique = true, nullable = false, length = 45)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password_", nullable = false)
	public byte[] getPassword() {
		return this.password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	@Column(name = "enabled", nullable = false)
	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	public Set<UserRole> getUserRole() {
		return this.userRole;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}

	@Column(name = "empID", nullable = false)
	public String getEmpid() {
		return empid;
	}

	public void setEmpid(String empid) {
		this.empid = empid;
	}

	@Column(name = "lastName", nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "firstName", nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "emailadd", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "salt", nullable = false)
	public byte[] getSalt() {
		return salt;
	}

	public void setSalt(byte[] salt) {
		this.salt = salt;
	}

	@Transient
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}

}