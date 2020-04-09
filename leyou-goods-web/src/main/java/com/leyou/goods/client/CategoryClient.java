package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RuXing
 * @create 2020-03-31 15:35
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
