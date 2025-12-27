package com.blog.dao;

import com.blog.model.Category;
import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    List<Category> findAll() throws SQLException;
    int insert(Category category) throws SQLException;
    int update(Category category) throws SQLException;
    int delete(Integer id) throws SQLException;
    Category findById(Integer id) throws SQLException;
}