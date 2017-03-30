package com.optum.technology.minerva.service;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.LineOfBusiness;

public interface LoBService {
	public List<LineOfBusiness> getAll() throws SQLException;
	public LineOfBusiness geLoB(int id) throws SQLException;
	public void addLoB(LineOfBusiness lob) throws SQLException;
}
