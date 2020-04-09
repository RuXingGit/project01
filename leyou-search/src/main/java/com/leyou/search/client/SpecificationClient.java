package com.leyou.search.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RuXing
 * @create 2020-03-31 15:36
 */
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
