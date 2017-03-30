package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.optum.technology.minerva.dao.UserDao;
import com.optum.technology.minerva.entity.LoginTries;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.entity.UserRole;

@Repository("userDao")
public class UserDaoImpl extends AbstractDao implements UserDao {

	@Override
	public UserInfo getUser(String username) {
		Criteria criteria = getSession().createCriteria(UserInfo.class);
		criteria.add(Restrictions.eq("username", username));
		return (UserInfo) criteria.uniqueResult();
	}

	@Override
	public void addUser(UserInfo user) {
		persist(user);
	}

	@Override
	public void updateUser(UserInfo user) {
		update(user);

	}

	@Override
	public void deleteUser(UserInfo user) {
		delete(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserInfo> getAllUsers() {
		Criteria criteria = getSession().createCriteria(UserInfo.class);
		return (List<UserInfo>) criteria.list();
	}

	@Override
	public void addUserRole(List<UserRole> roles) {
		for (UserRole role : roles) {
			persist(role);
		}

	}

	@Override
	public void deleteUserRole(Set<UserRole> roles) throws SQLException {
		for (UserRole role : roles) {
			delete(role);
		}
	}

	@Override
	public LoginTries getLoginTries(String username) throws SQLException {
		Criteria criteria = getSession().createCriteria(LoginTries.class);
		criteria.add(Restrictions.eq("username", username));
		return (LoginTries) criteria.uniqueResult();
	}

	@Override
	public void addLoginTries(LoginTries tries) throws SQLException {
		getSession().saveOrUpdate(tries);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserRole> getUsersByRole(UserRole role) throws SQLException {
		Criteria criteria = getSession().createCriteria(UserRole.class);
		criteria.add(Restrictions.eq("role", role.getRole()));
		return (List<UserRole>) criteria.list();
	}

}
