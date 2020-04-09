package com.leyou.goods.controller;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author RuXing
 * @create 2020-04-02 09:17
 */
@Controller
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    @GetMapping("/item/{id}.html")
    public String toItemPage(@PathVariable("id")Long id, Model model){

        Map<String, Object> data = goodsService.loadData(id);
        // 直接设置map作为返回数据    这样就不用在页面上每次都data.XXX调用数据了
        model.addAllAttributes(data);

        // 生成静态页面
        goodsHtmlService.createHtml(id,data);
        /**
         * 生成静态页面后对nginx配置
         *      现在本地查找页面，然后再去相关微服务服务器
         */


        return "item";
    }
}
