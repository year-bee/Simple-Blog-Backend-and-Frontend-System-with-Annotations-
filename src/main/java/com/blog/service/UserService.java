// src/main/java/com/blog/service/UserService.java

package com.blog.service;

import com.blog.model.User;

public interface UserService {

    /**
     * 用户登录逻辑
     * @param username 用户名
     * @param rawPassword 原始密码 (未加密)
     * @return 登录成功的 User 对象，如果失败返回 null
     * @throws Exception
     */
    User login(String username, String rawPassword) throws Exception;

    // ... 其他业务方法
}