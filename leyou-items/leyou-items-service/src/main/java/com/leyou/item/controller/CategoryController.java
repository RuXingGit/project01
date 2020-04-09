package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.api.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 12:21
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    // 根据多个id查询分类名称
    @GetMapping     // 通过请求/category 调用
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){
        List<String> names = categoryService.queryCategoryNameByIds(ids);
        if(CollectionUtils.isEmpty(names)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    // 根据菜单父级节点查询子节点
    @GetMapping("/list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(
            @RequestParam(value = "pid", defaultValue = "0") Long pid) {
        if (pid == null || pid < 0) {
            // 响应400 参数不合法
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            return ResponseEntity.badRequest().build();
        }
        // 查询数据库
        List<Category> categories = categoryService.queryCategoriesByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {
            // 响应404 没有找到数据
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            return ResponseEntity.notFound().build();
        }
        // 200 查询成功返回结果
        return ResponseEntity.ok(categories);
    }
}
