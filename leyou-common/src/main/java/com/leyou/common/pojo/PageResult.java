package com.leyou.common.pojo;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 13:29
 */
public class PageResult<T> {
    // 总记录数
    private Long total;
    // 总页数
    private Integer totalPage;
    private List<T> items;

    public PageResult() {
    }

    public PageResult(Long total, Integer totalPage, List<T> items) {
        this.total = total;
        this.totalPage = totalPage;
        this.items = items;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
