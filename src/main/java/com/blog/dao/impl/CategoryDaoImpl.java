package com.blog.dao.impl;

import com.blog.dao.CategoryDao;
import com.blog.model.Category;
import com.blog.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM blog_category ORDER BY sort_order DESC, create_time DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToCategory(rs));
            }
        }
        return list;
    }

    @Override
    public Category findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM blog_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToCategory(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int insert(Category category) throws SQLException {
        String sql = "INSERT INTO blog_category(name, description, sort_order) VALUES(?,?,?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getSortOrder() != null ? category.getSortOrder() : 0);
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(Category category) throws SQLException {
        String sql = "UPDATE blog_category SET name = ?, description = ?, sort_order = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getSortOrder());
            ps.setInt(4, category.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Integer id) throws SQLException {
        String sql = "DELETE FROM blog_category WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    /**
     * 私有辅助方法：将 ResultSet 的当前行映射为 Category 对象
     */
    private Category mapRowToCategory(ResultSet rs) throws SQLException {
        Category c = new Category();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setSortOrder(rs.getInt("sort_order"));
        c.setArticleCount(rs.getInt("article_count"));
        c.setCreateTime(rs.getTimestamp("create_time"));
        return c;
    }
}