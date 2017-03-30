package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.MinervaProperties;

public interface PropertiesDao {
	public List<MinervaProperties> getProperties() throws SQLException;
	public MinervaProperties getProperty(String key) throws SQLException;
	public void updateProperties(MinervaProperties prop) throws SQLException;
}
