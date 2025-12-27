package com.blog.service.impl;

import com.blog.dao.UserDao;
import com.blog.dao.impl.UserDaoImpl;
import com.blog.model.User;
import com.blog.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String encryptedPassword) throws Exception {

        System.out.println("========== 登录调试开始 ==========");
        System.out.println("1. 正在尝试登录的用户名: " + username);

        // 1. 根据用户名查找用户
        User user = userDao.findByUsername(username);

        if (user == null) {
            System.out.println("❌ 错误：数据库里没找到这个用户名！");
            System.out.println("========== 登录调试结束 ==========");
            return null;
        }

        // 2. 校验用户状态
        if (user.getStatus() != 1) {
            System.out.println("❌ 错误：用户状态不是 1 (被禁用)");
            throw new Exception("用户已被禁用");
        }

        // 3. 打印密码对比详情 (关键步骤！)
        System.out.println("2. 数据库里的密码 (期望值): [" + user.getPassword() + "]");
        System.out.println("3. 代码算出的密码 (实际值): [" + encryptedPassword + "]");

        boolean isMatch = encryptedPassword.equals(user.getPassword());
        System.out.println("4. 密码匹配结果: " + isMatch);

        if (isMatch) {
            System.out.println("✅ 登录成功！");
            System.out.println("========== 登录调试结束 ==========");
            return user;
        } else {
            System.out.println("❌ 登录失败：密码不匹配");
            System.out.println("========== 登录调试结束 ==========");
            return null;
        }
    }
}