package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.optum.technology.minerva.dao.DocumentDao;
import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.LineOfBusiness;
@Repository
public class DocumentDaoImpl extends AbstractDao implements DocumentDao {

	@Override
	public void addDocument(Document doc) throws SQLException {
		persist(doc);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getAllDocuments() throws SQLException {
		List<Document> docs = getSession().createQuery(
				"select doc from Document doc join doc.category cat "
						+ "where cat.parentCat = '-1'").list();
		return docs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findDocuments(Document doc) throws SQLException {
		Criteria criteria = getSession().createCriteria(Document.class);

		if (!StringUtils.isEmpty(doc.getTitle())) {
			criteria.add(Restrictions.ilike("title", doc.getTitle() + "%",
					MatchMode.END));
		}
		criteria.createAlias("updateUser", "uploader");
		if (!StringUtils.isEmpty(doc.getUpdateUser().getFirstName())) {

			criteria.add(Restrictions.ilike("uploader.firstName", doc
					.getUpdateUser().getFirstName()+ "%", MatchMode.END));
		}

		if (!StringUtils.isEmpty(doc.getUpdateUser().getLastName())) {

			criteria.add(Restrictions.ilike("uploader.lastName", doc
					.getUpdateUser().getLastName()+ "%", MatchMode.END));
		}

		if (!StringUtils.isEmpty(doc.getTags())) {
			String[] tags = doc.getTags().split(",");

			Disjunction orList = Restrictions.disjunction();
			for (String tag : tags) {
				orList.add(Restrictions.ilike("tags", "%" + tag + "%",
						MatchMode.END));
			}
			criteria.add(orList);
		}
		if (null != doc.getLineOfBusiness()) {
			criteria.createAlias("lineOfBusiness", "lob");
			criteria.add(Restrictions.eq("lob.id", doc.getLineOfBusiness()
					.getId()));
		}
		if (null != doc.getCreateDate()) {
			criteria.add(Restrictions.gt("createDate", doc.getCreateDate()));
		}

		if (null != doc.getCategory()) {
			criteria.createAlias("category", "cat");
			criteria.add(Restrictions.eq("cat.id", doc.getCategory().getId()));
		}

		if (null != doc.getSubCategory()) {
			criteria.createAlias("subCategory", "subCat");
			criteria.add(Restrictions.eq("subCategory.id", doc.getSubCategory()
					.getId()));
		}

		criteria.add(Restrictions.eq("enabled", doc.isEnabled()));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void updateDocument(Document doc) throws SQLException {
		update(doc);
	}

	@Override
	public Document getDocument(int docId) throws SQLException {
		Criteria criteria = getSession().createCriteria(Document.class);
		criteria.add(Restrictions.eq("id", docId));
		return (Document) criteria.uniqueResult();
	}

	@Override
	public void deleteDocument(Document doc) throws SQLException {
		delete(doc);

	}

	@Override
	public List<Document> getToolsAndReferences() throws SQLException {
		@SuppressWarnings("unchecked")
		List<Document> docs = getSession().createQuery(
				"select doc from Document doc join doc.category cat "
						+ "where cat.parentCat = '0'").list();
		return docs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> getDocumentsByDates(LocalDateTime start,
			LocalDateTime end, LineOfBusiness lob) throws SQLException {
		Criteria criteria = getSession().createCriteria(Document.class);

		criteria.add(Restrictions.between("createDate", start, end));
		if (lob.getId() != 0) {
			criteria.createAlias("lineOfBusiness", "lob");
			criteria.add(Restrictions.eq("lob.id", lob.getId()));
		}

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<Document>) criteria.list();
	}

}
