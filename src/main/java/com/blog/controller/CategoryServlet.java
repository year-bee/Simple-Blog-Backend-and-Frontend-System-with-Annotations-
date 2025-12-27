package com.blog.controller;

import com.blog.common.Result;
import com.blog.model.Category;
import com.blog.service.impl.CategoryService;
import com.blog.util.JsonUtil;
import com.blog.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/category/*")
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService = new CategoryService();
    private JsonUtil jsonUtil = new JsonUtil();
    private ObjectMapper objectMapper = new ObjectMapper(); // 用于解析请求体中的 JSON

    // 1. 查询 (GET)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || "/list".equals(pathInfo)) {
                // 获取全部分类列表
                List<Category> categories = categoryService.getAllCategories();
                jsonUtil.writeJson(resp, Result.success(categories));
            } else {
                // 根据 ID 获取单个分类，例如 /api/category/5
                String idStr = pathInfo.substring(1);
                int id = Integer.parseInt(idStr);
                Category category = categoryService.getCategoryById(id);
                jsonUtil.writeJson(resp, Result.success(category));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("查询失败: " + e.getMessage()));
        }
    }

    // 2. 新增 (POST)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // 从请求体中读取 JSON 并转换为 Category 对象
            Category category = objectMapper.readValue(req.getReader(), Category.class);

            // 基础校验
            if (ValidationUtil.isEmpty(category.getName())) {
                jsonUtil.writeJson(resp, Result.error("分类名称不能为空"));
                return;
            }

            if (categoryService.addCategory(category)) {
                jsonUtil.writeJson(resp, Result.success("添加成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("添加失败"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    // 3. 修改 (PUT)
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Category category = objectMapper.readValue(req.getReader(), Category.class);

            if (category.getId() == null) {
                jsonUtil.writeJson(resp, Result.error("修改失败：缺失分类ID"));
                return;
            }

            if (categoryService.updateCategory(category)) {
                jsonUtil.writeJson(resp, Result.success("修改成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("修改失败"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    // 4. 删除 (DELETE)
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // 获取 /5
        if (pathInfo == null || pathInfo.equals("/")) {
            jsonUtil.writeJson(resp, Result.error("请指定要删除的ID"));
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            if (categoryService.removeCategory(id)) {
                jsonUtil.writeJson(resp, Result.success("删除成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("删除失败，分类可能不存在"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("删除出错: " + e.getMessage()));
        }
    }
}