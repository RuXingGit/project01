package com.leyou.search.pojo;

import java.util.Map;

/**
 * @author RuXing
 * @create 2020-04-01 11:00
 */
public class SearchRequest {
    private String key;     // 搜索关键字
    private Integer page;   // 搜索的分页信息

    private Map<String,Object> filter;


    // 默认分页信息
    public static final Integer DEFAULT_PAGE=1;
    public static final Integer DEFAULT_SIZE=20;    // 默认页面大小不允许用户自定义

    public SearchRequest() {
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPage() {
        if(page==null){
            // 前端没有传入page参数就使用默认值
            return DEFAULT_PAGE;
        }
        // 页码不允许小于1
        return Math.max(DEFAULT_PAGE,page);
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    // 获取默认页面大小
    public static Integer getSize() {
        return DEFAULT_SIZE;
    }
}
