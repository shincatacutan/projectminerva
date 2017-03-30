package com.optum.technology.minerva.dao;

import java.sql.SQLException;
import java.util.List;

import com.optum.technology.minerva.entity.Category;

public interface CategoryDao {
	public List<Category> getAll() throws SQLException;
	public List<Category> getSubCategory(String cat) throws SQLException;
	public Category getCaterogy(String directoryName) throws SQLException;
	public Category getCaterogy(int id) throws SQLException;
	public void addCategory(Category cat) throws SQLException;
	public void deleteCategory(Category catId) throws SQLException;
}
