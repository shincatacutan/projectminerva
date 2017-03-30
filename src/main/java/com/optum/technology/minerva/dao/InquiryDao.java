package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.InquiryReply;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;

public interface InquiryDao {
	public Inquiry addInquiry(Inquiry inquiry) throws SQLException;
	public Inquiry getInquiry(int inqId) throws SQLException;
	public List<Inquiry> getInquiry(UserInfo user) throws SQLException;
	public void updateInquiry(Inquiry inquiry) throws SQLException;
	public List<Inquiry> viewAll() throws SQLException;
	public void deleteInquiry(Inquiry inquiry) throws SQLException;
	public void deleteReply(InquiryReply reply) throws SQLException;
	
	public List<InquiryReply> viewReplies(int inquiryID) throws SQLException;
	public InquiryReply getInquiryReply(int id) throws SQLException;
	public void addReply(InquiryReply reply) throws SQLException;
	public List<Inquiry> getInquiryByDates(String start, String end, LineOfBusiness lob) throws SQLException;
	public List<InquiryReply> getInquiryReplyByInq(String start, String end, LineOfBusiness lob) throws SQLException;

}
