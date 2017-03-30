package com.optum.technology.minerva.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.optum.technology.minerva.dao.CategoryDao;
import com.optum.technology.minerva.entity.Category;
import com.optum.technology.minerva.service.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Override
	public List<Category> getAll() throws SQLException {
		return categoryDao.getAll();
	}

	@Override
	public List<Category> getSubCategory(String cat) throws SQLException {
		return categoryDao.getSubCategory(cat);
	}

	@Override
	public Category getCaterogy(String directoryName) throws SQLException {
		return categoryDao.getCaterogy(directoryName);
	}

	@Override
	public void addCategory(Category cat) throws SQLException {
		categoryDao.addCategory(cat);
	}

	@Override
	public void deleteCategory(Category catId) throws SQLException {
		categoryDao.deleteCategory(catId);
		
	}

	@Override
	public Category getCaterogy(int id) throws SQLException {
		return categoryDao.getCaterogy(id);
	}

}
