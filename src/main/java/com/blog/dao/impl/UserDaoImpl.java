// src/main/java/com/blog/dao/impl/UserDaoImpl.java

package com.blog.dao.impl;

import com.blog.dao.UserDao;
import com.blog.model.User;
import com.blog.util.DBUtil;
import java.sql.*;

public class UserDaoImpl implements UserDao {

    @Override
    public User findByUsername(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM blog_user WHERE username = ?";
        User user = null;

        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                // 使用 rs 结果集填充 User 对象
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNickname(rs.getString("nickname"));
                user.setEmail(rs.getString("email"));
                user.setAvatar(rs.getString("avatar"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getInt("status"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                user.setUpdateTime(rs.getTimestamp("update_time"));
            }
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return user;
    }
}