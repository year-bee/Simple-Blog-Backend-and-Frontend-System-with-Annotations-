package com.blog.dao;
import com.blog.model.Article;
import java.sql.SQLException;
import java.util.List;

public interface ArticleDao {
    // 分页查询文章列表
    List<Article> findList(int page, int pageSize, String keyword) throws SQLException;
    // 查询总记录数（用于分页）
    long count(String keyword) throws SQLException;
    // 根据ID查询详情
    Article findById(Integer id) throws SQLException;

    int insert(Article article) throws SQLException;
    int update(Article article) throws SQLException;
    int delete(Integer id) throws SQLException;
}