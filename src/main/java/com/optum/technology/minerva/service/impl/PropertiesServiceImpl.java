package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.technology.minerva.dao.PropertiesDao;
import com.optum.technology.minerva.entity.MinervaProperties;
import com.optum.technology.minerva.service.PropertiesService;
@Service
@Transactional
public class PropertiesServiceImpl implements PropertiesService {

	@Autowired
	private PropertiesDao propertiesDao;
	
	@Override
	public List<MinervaProperties> getProperties() throws SQLException {
		return propertiesDao.getProperties();
	}

	@Override
	public void updateProperties(MinervaProperties prop) throws SQLException {
		propertiesDao.updateProperties(prop);
	}

	@Override
	public MinervaProperties getProperty(String key) throws SQLException {
		return propertiesDao.getProperty(key);
	}

}
