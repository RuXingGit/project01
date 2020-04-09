package com.leyou.user.controller;


import com.leyou.item.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author RuXing
 * @create 2020-04-03 10:29
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    // 用户登录
    @PostMapping("/query")
    public ResponseEntity<User> query(
            @RequestParam("username")String username,
            @RequestParam("password")String password){
        User user = userService.query(username,password);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // 用户注册
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid User user, @RequestParam("code")String code){
        Boolean bool = null;
        try {
            bool = userService.register(user, code);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.badRequest().body("用户名重复");
        }
        if(!bool){
            return ResponseEntity.badRequest().body("验证码不正确");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("注册成功");
    }

    // 发送验证码
    @PostMapping("/send")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone){
        userService.sendVerifyCode(phone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 手机号码和用户名验重
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> check(
            @PathVariable("data")String data,
            @PathVariable("type")Integer type){
        Boolean bool = userService.check(data,type);
        if(bool==null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bool);
    }
}
