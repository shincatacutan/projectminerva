package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.optum.technology.minerva.dao.LineOfBusinessDao;
import com.optum.technology.minerva.entity.LineOfBusiness;
@Repository
public class LoBDaoImpl extends AbstractDao implements LineOfBusinessDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<LineOfBusiness> getAll() throws SQLException {
		Criteria criteria = getSession().createCriteria(LineOfBusiness.class);
		return (List<LineOfBusiness>) criteria.list();
	}

	@Override
	public LineOfBusiness geLoB(int id) throws SQLException {
		Criteria criteria = getSession().createCriteria(LineOfBusiness.class);
		criteria.add(Restrictions.eq("id", id));
		return (LineOfBusiness) criteria.uniqueResult();
	}

	@Override
	public void addLoB(LineOfBusiness lob) throws SQLException {
		persist(lob);
	}

}
