// src/main/java/com/blog/util/JsonUtil.java

package com.blog.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.blog.common.Result; // 假设 Result 类已创建，在 com.blog.common 包下

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtil {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将 Result 对象转换为 JSON 写入 HttpServletResponse
     */
    public void writeJson(HttpServletResponse resp, Result<?> result) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK); // HTTP 状态码通常设为 200，业务错误由 Result.code 承载

        String json = objectMapper.writeValueAsString(result);
        resp.getWriter().write(json);
    }
}