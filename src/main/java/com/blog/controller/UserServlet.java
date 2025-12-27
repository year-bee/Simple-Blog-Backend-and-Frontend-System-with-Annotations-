package com.blog.controller;
import com.blog.util.EncryptUtil;
import com.blog.common.Result;
import com.blog.model.User;
import com.blog.service.UserService;
import com.blog.service.impl.UserServiceImpl;
import com.blog.util.EncryptUtil; // 【修改点1】引入加密工具类
import com.blog.util.JsonUtil;
import com.blog.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/api/user/*")
public class UserServlet extends HttpServlet {

    private UserService userService = new UserServiceImpl();
    private JsonUtil jsonUtil = new JsonUtil();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if ("/login".equals(pathInfo)) {
            login(req, resp);
        } else {
            resp.setStatus(404);
            jsonUtil.writeJson(resp, Result.error(404, "接口不存在"));
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            jsonUtil.writeJson(resp, Result.error(400, "用户名或密码不能为空"));
            return;
        }

        try {
            // ============================================================
            // 核心修改：先对用户输入的 "123456" 进行加密，变成 "b6a8..."
            // ============================================================
            String encryptedPassword = EncryptUtil.encrypt(password);

            // 然后拿着加密后的乱码去数据库比对
            User user = userService.login(username, encryptedPassword);

            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                session.setMaxInactiveInterval(30 * 60);
                // 注意：这里 user 对象里的 password 也会被转成 JSON 返回给前端
                // 生产环境建议把 user.setPassword(null) 清空一下再返回，更安全
                user.setPassword(null);
                jsonUtil.writeJson(resp, Result.success("登录成功", user));
            } else {
                jsonUtil.writeJson(resp, Result.error(400, "用户名或密码错误"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error(500, "登录失败: " + e.getMessage()));
            e.printStackTrace();
        }

    }
}