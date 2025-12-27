package com.blog.controller;

import com.blog.common.Result;
import com.blog.model.StatisticsVO;
import com.blog.util.DBUtil;
import com.blog.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/api/statistics/home")
public class StatisticsServlet extends HttpServlet {
    private JsonUtil jsonUtil = new JsonUtil();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StatisticsVO vo = new StatisticsVO();

        try (Connection conn = DBUtil.getConnection()) {
            // 1. 统计文章数
            vo.setArticleCount(getCount(conn, "SELECT count(*) FROM blog_article"));
            // 2. 统计分类数
            vo.setCategoryCount(getCount(conn, "SELECT count(*) FROM blog_category"));
            // 3. 统计标签数
            vo.setTagCount(getCount(conn, "SELECT count(*) FROM blog_tag"));
            // 4. 统计总阅读量
            vo.setViewCount(getCount(conn, "SELECT sum(view_count) FROM blog_article"));

            jsonUtil.writeJson(resp, Result.success(vo));
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("统计获取失败: " + e.getMessage()));
        }
    }

    private long getCount(Connection conn, String sql) {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}