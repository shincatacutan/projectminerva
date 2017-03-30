package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.optum.technology.minerva.dao.PropertiesDao;
import com.optum.technology.minerva.entity.MinervaProperties;

@Repository
public class PropertiesDaoImpl extends AbstractDao implements PropertiesDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<MinervaProperties> getProperties() throws SQLException {
		Criteria criteria = getSession().createCriteria(MinervaProperties.class);
		return (List<MinervaProperties>)criteria.list();
	}

	@Override
	public void updateProperties(MinervaProperties prop) throws SQLException {
		update(prop);
	}

	@Override
	public MinervaProperties getProperty(String key) throws SQLException {
		Criteria criteria = getSession().createCriteria(MinervaProperties.class);
		criteria.add(Restrictions.eq("id", key));
		return (MinervaProperties) criteria.uniqueResult();
	}

}
