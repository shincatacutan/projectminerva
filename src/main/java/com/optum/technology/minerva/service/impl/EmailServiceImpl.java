package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.optum.technology.minerva.dao.EmailDao;
import com.optum.technology.minerva.entity.Email;
import com.optum.technology.minerva.service.EmailService;

@Transactional
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailDao emailDao;

	@Override
	public List<Email> getUnsentEmails() throws SQLException {
		// TODO Auto-generated method stub
		return emailDao.getUnsentEmails();
	}

	@Override
	public void addUnsentEmail(Email email) throws SQLException {
		emailDao.addUnsentEmail(email);

	}

	@Override
	public void deleteUnsentEmail(Email email) throws SQLException {
		emailDao.deleteUnsentEmail(email);
	}

	@Override
	public Email getEmail(int id) throws SQLException {
		return emailDao.getEmail(id);
	}

	@Override
	public List<Email> getUnsentByUsername(String username) throws SQLException {
		return emailDao.getUnsentByUsername(username);
	}

}
