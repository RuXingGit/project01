package com.leyou.item.service.Impl;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import com.leyou.item.service.api.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author RuXing
 * @create 2020-03-27 12:20
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 根据分类信息父节点查询子节点
     * @param pid
     * @return 节点集合
     */
    @Override
    public List<Category> queryCategoriesByPid(Long pid) {
        Category record = new Category();
        record.setParentId(pid);
        // 使用基础mapper的查询方法
        return categoryMapper.select(record);
    }

    // 根据id集合查询名称
    @Override
    public List<String> queryCategoryNameByIds(List<Long> ids) {
        //
        List<Category> categories = categoryMapper.selectByIdList(ids);
//               等同于写法 category -> category.getName()
        return categories.stream().map(Category::getName).collect(Collectors.toList());
    }
}
