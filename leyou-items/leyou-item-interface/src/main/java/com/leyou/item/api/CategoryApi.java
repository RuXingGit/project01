package com.leyou.item.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 12:21
 */

@RequestMapping("/category")
public interface CategoryApi {

    // 根据多个id查询分类名称
    @GetMapping
    List<String> queryNamesByIds(@RequestParam("ids")List<Long> ids);
}
