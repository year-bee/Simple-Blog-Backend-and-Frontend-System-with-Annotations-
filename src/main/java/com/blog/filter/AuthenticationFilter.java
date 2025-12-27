// src/main/java/com/blog/filter/AuthenticationFilter.java

package com.blog.filter;

import com.blog.model.User;
import com.blog.util.JsonUtil;
import com.blog.common.Result;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// 拦截所有 /api/* 接口，除了公共接口
@WebFilter("/api/*")
public class AuthenticationFilter implements Filter {

    // 不需要登录的公共 API 路径
    private static final String[] IGNORE_API_PATHS = {
            "/api/user/login",
            "/api/user/register",
            "/api/article/public/list", // 假设公共查询文章接口
            "/api/category/list"        // 允许未登录查看分类
    };

    private JsonUtil jsonUtil = new JsonUtil();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getServletPath() + (req.getPathInfo() != null ? req.getPathInfo() : "");

        // 1. 检查是否在忽略列表中
        for (String ignorePath : IGNORE_API_PATHS) {
            if (path.startsWith(ignorePath)) {
                chain.doFilter(request, response); // 放行公共接口
                return;
            }
        }

        // 2. 检查登录状态
        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // 未登录，返回 401 Unauthorized
            resp.setStatus(401);
            jsonUtil.writeJson(resp, Result.error(401, "请先登录才能访问"));
            return;
        }

        // 3. (可选但推荐) 检查管理员权限
        if (path.startsWith("/api/admin")) {
            if (!"admin".equals(user.getRole())) {
                // 权限不足，返回 403 Forbidden
                resp.setStatus(403);
                jsonUtil.writeJson(resp, Result.error(403, "权限不足，无权访问"));
                return;
            }
        }

        // 登录且权限验证通过，放行
        chain.doFilter(request, response);
    }
}