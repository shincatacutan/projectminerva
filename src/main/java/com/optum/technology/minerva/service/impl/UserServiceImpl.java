package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.technology.minerva.dao.UserDao;
import com.optum.technology.minerva.entity.LoginTries;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;
import com.optum.technology.minerva.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final static Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userdao;

	@Override
	public UserInfo getUser(String lanId) throws SQLException {
		logger.debug("[service layer] getUser : " + lanId);
		UserInfo user = userdao.getUser(lanId);
		return user;
	}

	@Override
	public void updateUser(UserInfo user,List<UserRole> roles) throws SQLException {
		userdao.updateUser(user);
		userdao.deleteUserRole(user.getUserRole());
		userdao.addUserRole(roles);
	}

	@Override
	public void deleteUser(UserInfo user) throws SQLException {
		userdao.deleteUserRole(user.getUserRole());
		userdao.deleteUser(user);
	}

	@Override
	public List<UserInfo> getAllUsers() throws SQLException {
		return userdao.getAllUsers();
	}

	@Override
	public void addUser(UserInfo user, List<UserRole> roles) throws SQLException {
		userdao.addUser(user);
		userdao.addUserRole(roles);
	}

	@Override
	public void updateUser(UserInfo user) throws SQLException {
		userdao.updateUser(user);
	}

	@Override
	public LoginTries getLoginTries(String username) throws SQLException {
		return userdao.getLoginTries(username);
	}

	@Override
	public void addLoginTries(LoginTries tries) throws SQLException {
		userdao.addLoginTries(tries);
	}

	@Override
	public void deactivateUser(UserInfo user) throws SQLException {
		user.setEnabled(false);
		userdao.updateUser(user);
		
	}

	@Override
	public List<UserInfo> getUsersByRole(UserRole role) throws SQLException {
		List<UserRole> usersWithRole = userdao.getUsersByRole(role);
		List<UserInfo> usersFound = new ArrayList<UserInfo>();
		for(UserRole userRole: usersWithRole){
			usersFound.add(userdao.getUser(userRole.getUser()));
		}
		return usersFound;
	}

	@Override
	public String getEmailRecipients(String role) throws SQLException {
		UserRole userRole = new UserRole();
		userRole.setRole(role);
		StringBuilder sb = new StringBuilder();
		for(UserInfo info: getUsersByRole(userRole)){
			sb.append(info.getEmail()+",");
		}
		
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	

}
