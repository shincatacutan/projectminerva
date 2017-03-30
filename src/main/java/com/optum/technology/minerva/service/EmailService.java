package com.optum.technology.minerva.service;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.Email;

public interface EmailService {
	public List<Email> getUnsentEmails() throws SQLException;
	public void addUnsentEmail(Email email) throws SQLException;
	public void deleteUnsentEmail(Email email) throws SQLException;
	public Email getEmail (int id) throws SQLException;
	public List<Email> getUnsentByUsername(String username) throws SQLException;
}
