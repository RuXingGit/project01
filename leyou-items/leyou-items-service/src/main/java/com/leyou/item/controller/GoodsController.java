package com.leyou.item.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.service.api.GoodsService;
import com.leyou.item.vo.SpuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 17:40
 */
@Controller
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    // 根据skuId查询sku
    @GetMapping("/sku/{skuId}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("skuId")Long skuId){
        Sku sku = goodsService.querySkuById(skuId);
        if(sku==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sku);
    }

    // 根据spuId查询spu对象
    @GetMapping("{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id")Long id){
        Spu spu = goodsService.querySpuById(id);
        if(spu==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spu);
    }

    // 获取spu中的sku集合
    @GetMapping("/sku/list")
    public ResponseEntity<List<Sku>> querySkuListBySpuId(@RequestParam("id")Long spuId){
        List<Sku> skuList = goodsService.querySkuListBySpuId(spuId);
        if(CollectionUtils.isEmpty(skuList)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(skuList);
    }

    // 根据SpuId获取详细信息
    @GetMapping("/spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = goodsService.querySpuDetailById(spuId);
        if(spuDetail==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    // 更新信息
    @PutMapping("/goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuVO spuVO){
        goodsService.updateGoods(spuVO);
        return ResponseEntity.noContent().build();
    }


    // 新增商品
    @PostMapping("/goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuVO spuVO){
        goodsService.saveGoods(spuVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 分页查询spu
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuVO>> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows) {

        PageResult<SpuVO> result = goodsService.querySpuByPage(key, saleable, page, rows);
        if (CollectionUtils.isEmpty(result.getItems())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

}
