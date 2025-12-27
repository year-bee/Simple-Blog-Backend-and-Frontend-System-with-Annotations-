package com.blog.common;

import java.util.List;

public class PageResult<T> {
    private List<T> list;    // 当前页的数据列表
    private long total;      // 总记录数
    private int totalPages;  // 总页数

    public PageResult(List<T> list, long total, int pageSize) {
        this.list = list;
        this.total = total;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }

    // Getter & Setter
    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
