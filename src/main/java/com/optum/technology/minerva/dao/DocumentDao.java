package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;

import org.joda.time.LocalDateTime;

import com.optum.technology.minerva.entity.Document;
import com.optum.technology.minerva.entity.LineOfBusiness;

public interface DocumentDao {
	public void addDocument(Document doc) throws SQLException;
	public List<Document> getAllDocuments() throws SQLException;
	public List<Document> getToolsAndReferences() throws SQLException;
	public Document getDocument(int docId) throws SQLException;
	public List<Document> findDocuments(Document doc) throws SQLException;
	public void updateDocument(Document doc) throws SQLException;
	public void deleteDocument(Document doc) throws SQLException;
	public List<Document> getDocumentsByDates(LocalDateTime start,
			LocalDateTime end, LineOfBusiness lob) throws SQLException;
}