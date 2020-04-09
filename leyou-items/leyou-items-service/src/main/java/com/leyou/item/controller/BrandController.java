package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.api.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-27 13:43
 */
@Controller
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    // 根据id查询品牌
    @GetMapping("/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id")Long id){
        Brand brand = brandService.queryBrandById(id);
        if(brand==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }

    // 根据三级分类id查询品牌信息
    @GetMapping("/cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(
            @PathVariable("cid")Long cid){
        List<Brand> brands = brandService.queryBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    // 保存品牌信息
    @PostMapping
    public  ResponseEntity<Void> saveBrand(
            // 前端传的数据不能是对象 ，只能是键值对
        Brand brand, @RequestParam("cids")List<Long> cids){
        brandService.saveBrand(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 根据条件查询品牌信息，分页并排序
    @GetMapping("/page")
    public ResponseEntity<PageResult<Brand>> queryBrandListByPage(
            // 搜索关键字
            @RequestParam(value = "key", required = false) String key,
            // 搜索第几页
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            // 每页显示数据条数
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            // 根据哪个属性排序
            @RequestParam(value = "sortBy", required = false) String sortBy,
            // 升序降序
            @RequestParam(value = "desc", required = false) Boolean desc) {
        PageResult<Brand> pageResult = brandService.queryBrandListByPage(key,page,rows,sortBy,desc);
        if(!CollectionUtils.isEmpty(pageResult.getItems())){
            // 成功
            return ResponseEntity.ok(pageResult);
        }
        // 没有查询到结果
        return ResponseEntity.notFound().build();
    }
}
