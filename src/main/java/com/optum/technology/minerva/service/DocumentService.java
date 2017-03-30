package com.optum.technology.minerva.service;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.LineOfBusiness;

public interface DocumentService {
	public void addDocument(Document doc) throws SQLException;
	public List<Document> getAllDocuments(boolean isMainDocs) throws SQLException;
	public List<Document> searchDocument(boolean isMainDocs, String title,
			String firstName, String lastName, String tags, String dateCascaded, String mainCat,
			String subCat, String lineOfBus, boolean isArchived) throws SQLException;
	public Document getDocument(int docId) throws SQLException;
	public void updateDocument(Document doc) throws SQLException;
	public void deleteDocument(Document doc) throws SQLException;
	public List<Document> getDocumentsByDates(LocalDateTime start,
			LocalDateTime end, LineOfBusiness lob) throws SQLException;
}
