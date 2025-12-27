// src/main/java/com/blog/filter/CorsFilter.java

package com.blog.filter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 拦截所有 API 接口
@WebFilter("/api/*")
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 允许所有来源访问，实际项目中可以限制为前端的域名/端口
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));

        // 允许携带 Cookie/Session 信息
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        // 允许的请求方法
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");

        // 允许的请求头
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // 预检请求（OPTIONS）直接返回 200，不进入 Servlet 处理
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}