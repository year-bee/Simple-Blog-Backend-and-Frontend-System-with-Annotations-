// src/main/java/com/blog/dao/UserDao.java

package com.blog.dao;

import com.blog.model.User;
import java.sql.SQLException;

public interface UserDao {

    /**
     * 根据用户名查询用户，用于登录和注册校验
     * @param username 用户名
     * @return User 对象或 null
     * @throws SQLException
     */
    User findByUsername(String username) throws SQLException;

    // ... 其他方法，如：
    // void insertUser(User user) throws SQLException;
    // void updateUser(User user) throws SQLException;
    // List<User> findAll() throws SQLException;
}