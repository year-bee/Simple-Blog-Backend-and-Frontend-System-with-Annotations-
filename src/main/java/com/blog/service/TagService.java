package com.blog.service;
import com.blog.dao.TagDao;
import com.blog.dao.impl.TagDaoImpl;
import com.blog.model.Tag;
import java.sql.SQLException;
import java.util.List;

public class TagService {
    private TagDao tagDao = new TagDaoImpl();

    public List<Tag> getAllTags() throws SQLException {
        return tagDao.findAll();
    }

    public boolean addTag(Tag tag) throws SQLException {
        return tagDao.insert(tag) > 0;
    }

    public boolean updateTag(Tag tag) throws SQLException {
        return tagDao.update(tag) > 0;
    }

    public boolean removeTag(Integer id) throws SQLException {
        // 注意：后续需要在文章模块处理标签引用关系，如果标签正在被使用，可能需要限制删除
        return tagDao.delete(id) > 0;
    }
}
