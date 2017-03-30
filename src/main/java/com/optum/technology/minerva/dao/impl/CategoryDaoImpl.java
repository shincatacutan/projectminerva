package com.optum.technology.minerva.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.optum.technology.minerva.dao.CategoryDao;
import com.optum.technology.minerva.entity.Category;
@Repository
public class CategoryDaoImpl extends AbstractDao implements CategoryDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getAll() {
		Criteria criteria = getSession().createCriteria(Category.class);
		Criterion mainCat = Restrictions.eq("parentCat", "-1");
		Criterion secondaryCat = Restrictions.eq("parentCat", "0");
		LogicalExpression orExp = Restrictions.or(mainCat, secondaryCat);
		criteria.add(orExp);
		return (List<Category>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Category> getSubCategory(String cat) {
		Criteria criteria = getSession().createCriteria(Category.class);
		criteria.add(Restrictions.eq("parentCat", cat));
		return (List<Category>) criteria.list();
	}

	@Override
	public Category getCaterogy(String directoryName) {
		Criteria criteria = getSession().createCriteria(Category.class);
		criteria.add(Restrictions.eq("directoryName", directoryName));
		return (Category) criteria.uniqueResult();
	}

	@Override
	public void addCategory(Category cat) {
		persist(cat);
	}

	@Override
	public void deleteCategory(Category cat) throws SQLException {
		delete(cat);
		
	}

	@Override
	public Category getCaterogy(int id) throws SQLException {
		Criteria criteria = getSession().createCriteria(Category.class);
		criteria.add(Restrictions.eq("id", id));
		return (Category) criteria.uniqueResult();
	}

}
