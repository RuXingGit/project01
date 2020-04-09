package com.leyou.goods.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RuXing
 * @create 2020-03-31 15:34
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
