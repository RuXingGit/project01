package com.leyou.item.service.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.vo.SpuVO;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-03-28 17:36
 */
public interface GoodsService {
    PageResult<SpuVO> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    void saveGoods(SpuVO spuVO);

    SpuDetail querySpuDetailById(Long spuId);

    List<Sku> querySkuListBySpuId(Long spuId);

    void updateGoods(SpuVO spuVO);

    Spu querySpuById(Long id);

    Sku querySkuById(Long skuId);
}
