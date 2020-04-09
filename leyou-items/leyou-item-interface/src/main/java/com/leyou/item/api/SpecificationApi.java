package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 16:36
 */

@RequestMapping("/spec")
public interface SpecificationApi {

    // 根据spuId查询规格参数组和规格参数详细信息
    @GetMapping("/group/param/{cid}")
    List<SpecGroup> queryGroupWithParamByCid(@PathVariable("cid") Long cid);

    // 根据组的特定参数 获取组对象
    @GetMapping("/params")
    List<SpecParam> queryParams(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid",required = false)Long cid,
            @RequestParam(value = "generic",required = false)Boolean generic,
            @RequestParam(value = "searching",required = false)Boolean searching);
}
