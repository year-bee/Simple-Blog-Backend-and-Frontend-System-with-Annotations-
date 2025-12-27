package com.blog.service.impl;

import com.blog.common.PageResult;
import com.blog.dao.ArticleDao;
import com.blog.dao.impl.ArticleDaoImpl;
import com.blog.model.Article;
import java.sql.SQLException;
import java.util.List;

public class ArticleService {
    private ArticleDao articleDao = new ArticleDaoImpl();

    public PageResult<Article> getArticleList(int page, int pageSize, String keyword) throws SQLException {
        List<Article> list = articleDao.findList(page, pageSize, keyword);
        long total = articleDao.count(keyword);
        return new PageResult<>(list, total, pageSize);
    }

    public Article getArticleById(Integer id) throws SQLException {
        return articleDao.findById(id);
    }

    public boolean addArticle(Article article) throws SQLException {
        // 可以在这里处理摘要生成逻辑：如果 summary 为空，截取 content 前100字
        if (article.getSummary() == null || article.getSummary().isEmpty()) {
            String content = article.getContent();
            if (content != null) {
                article.setSummary(content.length() > 100 ? content.substring(0, 100) : content);
            }
        }
        return articleDao.insert(article) > 0;
    }

    public boolean deleteArticle(Integer id) throws SQLException {
        return articleDao.delete(id) > 0;
    }
}