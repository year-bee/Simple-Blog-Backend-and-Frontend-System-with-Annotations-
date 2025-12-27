package com.blog.model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class Article {
    private Integer id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Integer categoryId;
    private Integer authorId;
    private String status; // 'draft', 'published', 'deleted'
    private Integer viewCount;

    // --- 扩展字段 (数据库中没有，通过关联查询获取) ---
    private String categoryName;
    private String authorName; // 即 user.nickname

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}