package com.blog.dao.impl;
import com.blog.dao.TagDao;
import com.blog.model.Tag;
import com.blog.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDaoImpl implements TagDao {

    @Override
    public List<Tag> findAll() throws SQLException {
        List<Tag> list = new ArrayList<>();
        String sql = "SELECT * FROM blog_tag ORDER BY create_time DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Tag tag = new Tag();
                tag.setId(rs.getInt("id"));
                tag.setName(rs.getString("name"));
                tag.setCreateTime(rs.getTimestamp("create_time"));
                list.add(tag);
            }
        }
        return list;
    }

    @Override
    public Tag findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM blog_tag WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tag tag = new Tag();
                    tag.setId(rs.getInt("id"));
                    tag.setName(rs.getString("name"));
                    tag.setCreateTime(rs.getTimestamp("create_time"));
                    return tag;
                }
            }
        }
        return null;
    }

    @Override
    public int insert(Tag tag) throws SQLException {
        String sql = "INSERT INTO blog_tag(name) VALUES(?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tag.getName());
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(Tag tag) throws SQLException {
        String sql = "UPDATE blog_tag SET name = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tag.getName());
            ps.setInt(2, tag.getId());
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Integer id) throws SQLException {
        String sql = "DELETE FROM blog_tag WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }
}
