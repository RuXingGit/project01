package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.vo.SpuVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-31 15:17
 */

public interface GoodsApi {

    // 根据skuId查询sku
    @GetMapping("/sku/{skuId}")
    Sku querySkuById(@PathVariable("skuId")Long skuId);

    // 根据spuId查询spu对象
    @GetMapping("{id}")
    Spu querySpuById(@PathVariable("id")Long id);

    // 根据SpuId获取详细信息,返回值SpuDetail方便直接使用
    @GetMapping("/spu/detail/{spuId}")
    SpuDetail querySpuDetailById(@PathVariable("spuId")Long spuId);

    // 分也查询spu信息
    @GetMapping("/spu/page")
    PageResult<SpuVO> querySpuByPage(
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows);

    // 根据spuId获取sku集合
    @GetMapping("/sku/list")
    List<Sku> querySkuListBySpuId(@RequestParam("id")Long spuId);
}
