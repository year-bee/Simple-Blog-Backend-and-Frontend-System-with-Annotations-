package com.blog.controller;

import com.blog.common.Result;
import com.blog.model.Article;
import com.blog.model.User;
import com.blog.util.DBUtil;
import com.blog.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/api/article/*")
public class ArticleServlet extends HttpServlet {

    private JsonUtil jsonUtil = new JsonUtil();
    private ObjectMapper objectMapper = new ObjectMapper();

    // 1. 新增文章
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        createArticle(req, resp);
    }

    // 2. 修改文章 (复用发布逻辑，做成 SaveOrUpdate)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        updateArticle(req, resp);
    }

    // 3. 删除文章 【之前缺这个，所以删除不行】
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            jsonUtil.writeJson(resp, Result.error(400, "参数错误，缺少文章ID"));
            return;
        }
        // 解析 ID: /123 -> 123
        String idStr = pathInfo.substring(1);
        deleteArticle(req, resp, idStr);
    }

    // 4. 查询 (列表 或 详情)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        // 兼容 /api/article, /api/article/, /api/article/list
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/list")) {
            getArticleList(req, resp);
        } else {
            // 获取详情 /api/article/123
            getArticleDetail(req, resp, pathInfo.substring(1));
        }
    }

    // --- 具体的业务逻辑方法 ---

    private void deleteArticle(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        // 权限校验
        if (!checkLogin(req, resp)) return;

        String sql = "DELETE FROM blog_article WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                jsonUtil.writeJson(resp, Result.success("删除成功"));
            } else {
                jsonUtil.writeJson(resp, Result.error(404, "文章不存在或已删除"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonUtil.writeJson(resp, Result.error("删除失败: " + e.getMessage()));
        }
    }

    private void updateArticle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!checkLogin(req, resp)) return;

        Article article = getArticleFromJson(req);
        if (article.getId() == null) {
            jsonUtil.writeJson(resp, Result.error("更新失败：缺少文章ID"));
            return;
        }

        String sql = "UPDATE blog_article SET title=?, content=?, summary=?, category_id=?, status=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, article.getTitle());
            ps.setString(2, article.getContent());
            ps.setString(3, getSummary(article));
            ps.setObject(4, article.getCategoryId());
            ps.setString(5, article.getStatus());
            ps.setLong(6, article.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                jsonUtil.writeJson(resp, Result.success("更新成功"));
            } else {
                jsonUtil.writeJson(resp, Result.error("更新失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    private void createArticle(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 1. 权限校验
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            jsonUtil.writeJson(resp, Result.error(401, "请先登录"));
            return;
        }
        User user = (User) session.getAttribute("user");

        // 2. 获取数据
        Article article = getArticleFromJson(req);

        // 3. 插入数据库
        String sql = "INSERT INTO blog_article (title, content, summary, category_id, author_id, status, create_time) VALUES (?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, article.getTitle());
            ps.setString(2, article.getContent());
            ps.setString(3, getSummary(article));
            ps.setObject(4, article.getCategoryId());
            ps.setLong(5, user.getId());
            ps.setString(6, article.getStatus());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                jsonUtil.writeJson(resp, Result.success("发布成功"));
            } else {
                jsonUtil.writeJson(resp, Result.error("发布失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    private void getArticleList(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 增加搜索功能的 SQL (简单实现)
        String keyword = req.getParameter("keyword");
        String sql = "SELECT a.*, u.nickname as authorName, c.name as categoryName " +
                "FROM blog_article a " +
                "LEFT JOIN blog_user u ON a.author_id = u.id " +
                "LEFT JOIN blog_category c ON a.category_id = c.id ";

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += "WHERE a.title LIKE ? ";
        }

        sql += "ORDER BY a.create_time DESC LIMIT 20";

        List<Map<String, Object>> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(1, "%" + keyword + "%");
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("id", rs.getLong("id"));
                map.put("title", rs.getString("title"));
                map.put("summary", rs.getString("summary"));
                map.put("status", rs.getString("status"));
                map.put("createTime", rs.getString("create_time"));
                map.put("authorName", rs.getString("authorName"));
                map.put("categoryName", rs.getString("categoryName"));
                list.add(map);
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("list", list);
            result.put("total", list.size());

            jsonUtil.writeJson(resp, Result.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            jsonUtil.writeJson(resp, Result.error("获取列表失败: " + e.getMessage()));
        }
    }

    private void getArticleDetail(HttpServletRequest req, HttpServletResponse resp, String id) throws IOException {
        String sql = "SELECT * FROM blog_article WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Article article = new Article();
                article.setId((int) rs.getLong("id"));
                article.setTitle(rs.getString("title"));
                article.setContent(rs.getString("content"));
                article.setCategoryId((int) rs.getLong("category_id"));
                article.setStatus(rs.getString("status"));
                article.setSummary(rs.getString("summary"));
                jsonUtil.writeJson(resp, Result.success(article));
            } else {
                jsonUtil.writeJson(resp, Result.error(404, "文章不存在"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonUtil.writeJson(resp, Result.error("获取详情失败: " + e.getMessage()));
        }
    }

    // --- 辅助工具方法 ---

    private boolean checkLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            jsonUtil.writeJson(resp, Result.error(401, "请先登录"));
            return false;
        }
        return true;
    }

    private Article getArticleFromJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = req.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return objectMapper.readValue(sb.toString(), Article.class);
    }

    private String getSummary(Article article) {
        String summary = article.getSummary();
        if (summary == null || summary.trim().isEmpty()) {
            String text = article.getContent().replaceAll("<[^>]+>", "");
            summary = text.length() > 100 ? text.substring(0, 100) + "..." : text;
        }
        return summary;
    }
}