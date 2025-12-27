package com.blog.service.impl;

import com.blog.dao.CategoryDao;
import com.blog.dao.impl.CategoryDaoImpl;
import com.blog.model.Category;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    public List<Category> getAllCategories() throws SQLException {
        return categoryDao.findAll();
    }

    public Category getCategoryById(Integer id) throws SQLException {
        return categoryDao.findById(id);
    }

    public boolean addCategory(Category category) throws SQLException {
        return categoryDao.insert(category) > 0;
    }

    public boolean updateCategory(Category category) throws SQLException {
        return categoryDao.update(category) > 0;
    }

    public boolean removeCategory(Integer id) throws SQLException {
        // 后续可以增加判断：如果该分类下 article_count > 0，则不允许删除
        return categoryDao.delete(id) > 0;
    }
}