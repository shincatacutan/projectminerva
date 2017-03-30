package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.technology.minerva.dao.LineOfBusinessDao;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.service.LoBService;
@Service
@Transactional
public class LoBServiceImpl implements LoBService {

	@Autowired
	private LineOfBusinessDao lobDao;

	@Override
	public List<LineOfBusiness> getAll() throws SQLException {
		return lobDao.getAll();
	}

	@Override
	public LineOfBusiness geLoB(int id) throws SQLException {
		return lobDao.geLoB(id);
	}

	@Override
	public void addLoB(LineOfBusiness lob) throws SQLException {
		lobDao.addLoB(lob);
	}

}
