package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.optum.technology.minerva.entity.LoginTries;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;

public interface UserDao {
	public UserInfo getUser(String username) throws SQLException;
	public void addUser (UserInfo user) throws SQLException;
	public void updateUser (UserInfo user) throws SQLException;
	public void deleteUser (UserInfo user) throws SQLException;
	public List<UserInfo> getAllUsers() throws SQLException;
	public void addUserRole(List<UserRole> roles) throws SQLException;
	public void deleteUserRole(Set<UserRole> roles) throws SQLException;
	public LoginTries getLoginTries(String username) throws SQLException;
	public void addLoginTries(LoginTries tries) throws SQLException;
	public List<UserRole> getUsersByRole(UserRole role) throws SQLException;
}
