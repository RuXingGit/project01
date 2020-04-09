package com.leyou.item.api;


import com.leyou.item.pojo.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author RuXing
 * @create 2020-04-03 17:57
 */
public interface UserApi {
    @PostMapping("/query")
    User query(
            @RequestParam("username")String username,
            @RequestParam("password")String password);
}
