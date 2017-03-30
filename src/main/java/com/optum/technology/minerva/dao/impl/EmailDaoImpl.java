package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.optum.technology.minerva.dao.EmailDao;
import com.optum.technology.minerva.entity.Email;

@Repository
public class EmailDaoImpl extends AbstractDao implements EmailDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Email> getUnsentEmails() throws SQLException {
		Criteria criteria = getSession().createCriteria(Email.class);
		return (List<Email>) criteria.list();
	}

	@Override
	public void addUnsentEmail(Email email) throws SQLException {
		persist(email);

	}

	@Override
	public void deleteUnsentEmail(Email email) throws SQLException {
		delete(email);
	}

	@Override
	public Email getEmail(int id) throws SQLException {
		Criteria criteria = getSession().createCriteria(Email.class);
		criteria.add(Restrictions.eq("id", id));
		return (Email) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Email> getUnsentByUsername(String username) throws SQLException {
		Criteria criteria = getSession().createCriteria(Email.class);
		criteria.add(Restrictions.eq("sender", username));
		return (List<Email>) criteria.list();
	}

}
