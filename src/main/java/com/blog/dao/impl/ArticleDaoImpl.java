package com.blog.dao.impl;

import com.blog.dao.ArticleDao;
import com.blog.model.Article;
import com.blog.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleDaoImpl implements ArticleDao {

    @Override
    public List<Article> findList(int page, int pageSize, String keyword) throws SQLException {
        List<Article> list = new ArrayList<>();
        // 核心 SQL：关联 category 表和 user 表
        // Limit 计算：offset = (page - 1) * pageSize
        StringBuilder sql = new StringBuilder(
                "SELECT a.id, a.title, a.summary, a.status, a.view_count, a.create_time, a.update_time, " +
                        "c.name as category_name, u.nickname as author_name " +
                        "FROM blog_article a " +
                        "LEFT JOIN blog_category c ON a.category_id = c.id " +
                        "LEFT JOIN blog_user u ON a.author_id = u.id " +
                        "WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND a.title LIKE ? ");
        }
        sql.append("ORDER BY a.create_time DESC LIMIT ?, ?");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            // 计算分页偏移量
            int offset = (page - 1) * pageSize;
            ps.setInt(paramIndex++, offset);
            ps.setInt(paramIndex, pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setSummary(rs.getString("summary"));
                a.setStatus(rs.getString("status"));
                a.setViewCount(rs.getInt("view_count"));
                a.setCategoryName(rs.getString("category_name")); // 填充关联字段
                a.setAuthorName(rs.getString("author_name"));     // 填充关联字段
                a.setCreateTime(rs.getTimestamp("create_time"));
                a.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(a);
            }
        }
        return list;
    }

    @Override
    public long count(String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM blog_article WHERE 1=1 ");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND title LIKE ?");
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public Article findById(Integer id) throws SQLException {
        // 详情页需要查出 content
        String sql = "SELECT * FROM blog_article WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Article a = new Article();
                a.setId(rs.getInt("id"));
                a.setTitle(rs.getString("title"));
                a.setContent(rs.getString("content")); // 详情页需要内容
                a.setSummary(rs.getString("summary"));
                a.setCategoryId(rs.getInt("category_id"));
                a.setAuthorId(rs.getInt("author_id"));
                a.setStatus(rs.getString("status"));
                return a;
            }
        }
        return null;
    }

    @Override
    public int insert(Article a) throws SQLException {
        String sql = "INSERT INTO blog_article(title, content, summary, category_id, author_id, status) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getTitle());
            ps.setString(2, a.getContent());
            ps.setString(3, a.getSummary());
            ps.setObject(4, a.getCategoryId()); // 使用 setObject 允许为空
            ps.setInt(5, a.getAuthorId());
            ps.setString(6, a.getStatus() != null ? a.getStatus() : "draft");
            return ps.executeUpdate();
        }
    }

    // update 和 delete 请参照 insert 自行补全，逻辑类似
    @Override
    public int update(Article article) throws SQLException {
        return 0; // 暂时留空，记得补全
    }

    @Override
    public int delete(Integer id) throws SQLException {
        String sql = "DELETE FROM blog_article WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}