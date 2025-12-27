package com.blog.model;

public class StatisticsVO {
    private long articleCount;
    private long categoryCount;
    private long tagCount;
    private long viewCount; // 所有文章阅读量总和

    // Getter & Setter (请补全或使用 Lombok)
    public long getArticleCount() { return articleCount; }
    public void setArticleCount(long articleCount) { this.articleCount = articleCount; }
    public long getCategoryCount() { return categoryCount; }
    public void setCategoryCount(long categoryCount) { this.categoryCount = categoryCount; }
    public long getTagCount() { return tagCount; }
    public void setTagCount(long tagCount) { this.tagCount = tagCount; }
    public long getViewCount() { return viewCount; }
    public void setViewCount(long viewCount) { this.viewCount = viewCount; }
}