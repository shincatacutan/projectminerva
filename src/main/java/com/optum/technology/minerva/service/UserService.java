package com.optum.technology.minerva.service;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.LoginTries;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;

public interface UserService {
	public UserInfo getUser(String lanID) throws SQLException;
	public void addUser(UserInfo user, List<UserRole> roles)
			throws SQLException;
	public void updateUser(UserInfo user, List<UserRole> roles)
			throws SQLException;
	public void updateUser(UserInfo user) throws SQLException;
	public void deleteUser(UserInfo user) throws SQLException;
	public List<UserInfo> getAllUsers() throws SQLException;
	public LoginTries getLoginTries(String username) throws SQLException;
	public void addLoginTries(LoginTries tries) throws SQLException;
	public void deactivateUser(UserInfo user) throws SQLException;
	public List<UserInfo> getUsersByRole(UserRole role) throws SQLException;
	public String getEmailRecipients(String role) throws SQLException;
}
