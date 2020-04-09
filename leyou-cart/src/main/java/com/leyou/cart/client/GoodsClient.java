package com.leyou.cart.client;

import com.leyou.item.api.GoodsApi;
import com.leyou.item.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author RuXing
 * @create 2020-04-05 12:00
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
