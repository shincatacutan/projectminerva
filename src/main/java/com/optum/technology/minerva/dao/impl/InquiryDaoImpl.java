package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.optum.technology.minerva.dao.InquiryDao;
import com.optum.technology.minerva.entity.Inquiry;
import com.optum.technology.minerva.entity.InquiryReply;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.util.MinervaUtils;

@Repository
public class InquiryDaoImpl extends AbstractDao implements InquiryDao {

	@Override
	public Inquiry addInquiry(Inquiry inquiry) {
		persist(inquiry);
		return inquiry;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inquiry> viewAll() {
		Criteria criteria = getSession().createCriteria(Inquiry.class);
		return (List<Inquiry>) criteria.list();
	}

	@Override
	public void addReply(InquiryReply reply) {
		persist(reply);

	}

	@Override
	public Inquiry getInquiry(int inqID) {
		Criteria criteria = getSession().createCriteria(Inquiry.class);
		criteria.add(Restrictions.eq("id", inqID));
		return (Inquiry) criteria.uniqueResult();
	}

	@Override
	public void updateInquiry(Inquiry inquiry) {
		update(inquiry);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inquiry> getInquiry(UserInfo user) {
		Criteria criteria = getSession().createCriteria(Inquiry.class);
		criteria.add(Restrictions.eq("createUser", user));

		return (List<Inquiry>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InquiryReply> viewReplies(int id) {
		Criteria criteria = getSession().createCriteria(InquiryReply.class);
		criteria.createAlias("inquiryId", "inquiry");
		criteria.add(Restrictions.eq("inquiry.id", id));

		return (List<InquiryReply>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Inquiry> getInquiryByDates(String start, String end, LineOfBusiness lob) throws SQLException {
		Criteria criteria = getSession().createCriteria(Inquiry.class);
		if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			criteria.add(
					Restrictions.between("createDate", MinervaUtils.parseDate(start), MinervaUtils.parseDate(end)));
		} else if (!StringUtils.isEmpty(start)) {
			criteria.add(Restrictions.ge("createDate", MinervaUtils.parseDate(start)));
		} else if (!StringUtils.isEmpty(end)) {
			criteria.add(Restrictions.le("createDate", MinervaUtils.parseDate(end)));
		}
		if (lob.getId() != 0) {
			criteria.createAlias("lineOfBusiness", "lob");
			criteria.add(Restrictions.eq("lob.id", lob.getId()));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Inquiry>) criteria.list();
	}

	@Override
	public void deleteInquiry(Inquiry inquiry) throws SQLException {
		delete(inquiry);
	}

	@Override
	public void deleteReply(InquiryReply reply) throws SQLException {
		delete(reply);
	}

	@Override
	public InquiryReply getInquiryReply(int id) throws SQLException {
		Criteria criteria = getSession().createCriteria(InquiryReply.class);
		criteria.add(Restrictions.eq("id", id));

		return (InquiryReply) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InquiryReply> getInquiryReplyByInq(String start, String end, LineOfBusiness lob) throws SQLException {
		
		Criteria criteria = getSession().createCriteria(InquiryReply.class);
		criteria.createAlias("inquiryId", "inquiry");
		if (!StringUtils.isEmpty(start) && !StringUtils.isEmpty(end)) {
			criteria.add(Restrictions.between("inquiry.createDate", MinervaUtils.parseDate(start),
					MinervaUtils.parseDate(end)));
		} else if (!StringUtils.isEmpty(start)) {
			criteria.add(Restrictions.ge("inquiry.createDate", MinervaUtils.parseDate(start)));
		} else if (!StringUtils.isEmpty(end)) {
			criteria.add(Restrictions.le("inquiry.createDate", MinervaUtils.parseDate(end)));
		}

		if (lob.getId() != 0) {
			criteria.createAlias("inquiry.lineOfBusiness", "lob");
			criteria.add(Restrictions.eq("lob.id", lob.getId()));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<InquiryReply>) criteria.list();
	}

}
