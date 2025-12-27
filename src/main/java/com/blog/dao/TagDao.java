package com.blog.dao;
import com.blog.model.Tag;
import java.sql.SQLException;
import java.util.List;

public interface TagDao {
    List<Tag> findAll() throws SQLException;
    Tag findById(Integer id) throws SQLException;
    int insert(Tag tag) throws SQLException;
    int update(Tag tag) throws SQLException;
    int delete(Integer id) throws SQLException;
}