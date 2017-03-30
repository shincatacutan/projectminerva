package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.technology.minerva.dao.InquiryDao;
import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.InquiryReply;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.service.InquiryService;

@Service
@Transactional
public class InquiryServiceImpl implements InquiryService {
	private final static Logger logger = LoggerFactory.getLogger(InquiryServiceImpl.class);
	@Autowired
	private InquiryDao inquiryDao;

	@Override
	public Inquiry addInquiry(Inquiry inquiry) throws SQLException {
		return inquiryDao.addInquiry(inquiry);
	}

	@Override
	public List<Inquiry> viewAll() throws SQLException {
		return inquiryDao.viewAll();
	}

	@Override
	public void addReply(InquiryReply reply) throws SQLException {
		inquiryDao.addReply(reply);
	}

	@Override
	public Inquiry getInquiry(int inqID) throws SQLException {
		return inquiryDao.getInquiry(inqID);
	}

	@Override
	public void updateInquiry(Inquiry inquiry) throws SQLException {
		inquiryDao.updateInquiry(inquiry);
	}

	@Override
	public List<Inquiry> getInquiry(UserInfo user) throws SQLException {
		return inquiryDao.getInquiry(user);
	}

	@Override
	public List<InquiryReply> viewReplies(int id) throws SQLException {
		return inquiryDao.viewReplies(id);
	}

	@Override
	public List<Inquiry> getInquiryByDates(String start, String end, LineOfBusiness lob) throws SQLException {
		return inquiryDao.getInquiryByDates(start, end, lob);
	}

	@Override
	public void deleteInquiry(Inquiry inquiry) throws SQLException {
	
		 inquiryDao.deleteInquiry(inquiry);
	}

	@Override
	public InquiryReply getInquiryReply(int id) throws SQLException {
		return inquiryDao.getInquiryReply(id);
	}

	@Override
	public List<InquiryReply> getInquiryReplyByInq(String start, String end, LineOfBusiness lob) throws SQLException {
		return inquiryDao.getInquiryReplyByInq(start, end, lob);
	}
}
