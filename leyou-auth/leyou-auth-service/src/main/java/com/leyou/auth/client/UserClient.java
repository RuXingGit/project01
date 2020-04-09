package com.leyou.auth.client;


import com.leyou.item.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author RuXing
 * @create 2020-04-03 17:58
 */
@FeignClient("user-service")
public interface UserClient extends UserApi {
}
