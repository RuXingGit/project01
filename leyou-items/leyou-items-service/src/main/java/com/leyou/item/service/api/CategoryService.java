package com.leyou.item.service.api;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 12:19
 */
public interface CategoryService {
    List<Category> queryCategoriesByPid(Long pid);
    List<String> queryCategoryNameByIds(List<Long> ids);
}
