package com.optum.technology.minerva.service;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.MinervaProperties;

public interface PropertiesService {
	public List<MinervaProperties> getProperties() throws SQLException;
	public void updateProperties(MinervaProperties prop) throws SQLException;
	public MinervaProperties getProperty(String key) throws SQLException;
}
