package com.leyou.goods.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RuXing
 * @create 2020-03-31 15:12
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
