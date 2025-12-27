package com.blog.controller;
import com.blog.common.Result;
import com.blog.model.Tag;
import com.blog.util.JsonUtil;
import com.blog.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.blog.service.TagService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/tag/*")
public class TagServlet extends HttpServlet {

    private TagService tagService = new TagService();
    private JsonUtil jsonUtil = new JsonUtil();
    private ObjectMapper objectMapper = new ObjectMapper();

    // GET: 获取列表或单个标签
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/list".equals(pathInfo)) {
                List<Tag> tags = tagService.getAllTags();
                jsonUtil.writeJson(resp, Result.success(tags));
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                // 也可以根据需要实现 getTagById
                jsonUtil.writeJson(resp, Result.success("接口开发中", null));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("查询失败: " + e.getMessage()));
        }
    }

    // POST: 新增标签
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Tag tag = objectMapper.readValue(req.getReader(), Tag.class);
            if (ValidationUtil.isEmpty(tag.getName())) {
                jsonUtil.writeJson(resp, Result.error("标签名称不能为空"));
                return;
            }
            if (tagService.addTag(tag)) {
                jsonUtil.writeJson(resp, Result.success("标签添加成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("标签添加失败"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    // PUT: 修改标签
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Tag tag = objectMapper.readValue(req.getReader(), Tag.class);
            if (tag.getId() == null || ValidationUtil.isEmpty(tag.getName())) {
                jsonUtil.writeJson(resp, Result.error("参数不完整"));
                return;
            }
            if (tagService.updateTag(tag)) {
                jsonUtil.writeJson(resp, Result.success("修改成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("修改失败"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }

    // DELETE: 删除标签
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            jsonUtil.writeJson(resp, Result.error("请指定删除ID"));
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            if (tagService.removeTag(id)) {
                jsonUtil.writeJson(resp, Result.success("删除成功", null));
            } else {
                jsonUtil.writeJson(resp, Result.error("删除失败"));
            }
        } catch (Exception e) {
            jsonUtil.writeJson(resp, Result.error("系统错误: " + e.getMessage()));
        }
    }
}