package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.optum.technology.minerva.dao.DocumentDao;
import com.optum.technology.minerva.entity.Category;
import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.LineOfBusiness;
import com.optum.technology.minerva.entity.UserInfo;
import com.optum.technology.minerva.service.DocumentService;
import com.optum.technology.minerva.service.LoBService;
@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	public DocumentDao documentDao;
	
	@Autowired
	public LoBService lobservice;

	@Override
	public void addDocument(Document doc) throws SQLException {
		documentDao.addDocument(doc);
	}

	@Override
	public List<Document> getAllDocuments(boolean isMainDocs)
			throws SQLException {
		if (!isMainDocs) {
			return documentDao.getToolsAndReferences();
		}
		return documentDao.getAllDocuments();
	}

	@Override
	public Document getDocument(int docId) throws SQLException {
		return documentDao.getDocument(docId);
	}

	@Override
	public void updateDocument(Document doc) throws SQLException {
		documentDao.updateDocument(doc);

	}

	@Override
	public void deleteDocument(Document doc) throws SQLException {
		documentDao.deleteDocument(doc);
	}

	@Override
	public List<Document> searchDocument(boolean isMainDocs, String title,
			String firstName, String lastName, String tags, String dateCascaded, String mainCat,
			String subCat, String lineOfBus, boolean isArchived) throws SQLException {
		// TODO Auto-generated method stub
		Document doc = new Document();
		doc.setTitle(title);
		
		if (!StringUtils.isEmpty(mainCat)) {
			Category cat = new Category();
			cat.setId(Integer.parseInt(mainCat));
			doc.setCategory(cat);
		}
		
		if (!StringUtils.isEmpty(lineOfBus)) {
			LineOfBusiness lob = new LineOfBusiness();
			lob.setId(Integer.parseInt(lineOfBus));
			doc.setLineOfBusiness(lob);
		}
		
		if (!StringUtils.isEmpty(subCat)) {
			Category subCategory = new Category();
			subCategory.setId(Integer.parseInt(subCat));
			doc.setSubCategory(subCategory);
		}
		
		doc.setTags(tags);
		if (!StringUtils.isEmpty(dateCascaded)) {
			String[] dateSplit = dateCascaded.split("/");
			doc.setCreateDate(new LocalDateTime(Integer.parseInt(dateSplit[2]),
					Integer.parseInt(dateSplit[0]), Integer
							.parseInt(dateSplit[1]), 0, 0));
		}

		UserInfo info = new UserInfo();
		info.setFirstName(firstName);
		info.setLastName(lastName);
		doc.setUpdateUser(info);
		doc.setEnabled(isArchived);
		return documentDao.findDocuments(doc);
	}

	@Override
	public List<Document> getDocumentsByDates(LocalDateTime start,
			LocalDateTime end, LineOfBusiness lob) throws SQLException {
		// TODO Auto-generated method stub
		return documentDao.getDocumentsByDates(start, end, lob);
	}

}
