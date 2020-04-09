package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.utils.CookieUtils;
import com.leyou.item.pojo.User;
import com.netflix.ribbon.proxy.annotation.Http;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RuXing
 * @create 2020-04-03 17:42
 */
@Controller
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    // 通过cookie的信息对用户登录状态进行验证
    @GetMapping("/verify")
    public ResponseEntity<UserInfo> verify(
            @CookieValue("LY_TOKEN")String token,
            HttpServletRequest request,
            HttpServletResponse response){
        if(StringUtils.isBlank(token)){
            return ResponseEntity.badRequest().build();
        }
        try {
            // 通过公钥解析token
            UserInfo user = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 身份校验未通过
            if(user==null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // 解析成功后刷新jwt（重新生成）
            token = JwtUtils.generateToken(user,jwtProperties.getPrivateKey(),jwtProperties.getExpire());

            // cookie的过期时间
            CookieUtils.setCookie(request,response,
                    jwtProperties.getCookieName(),
                    token,
                    jwtProperties.getExpire()*60);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    // 用户登录，生成JWT证书
    @PostMapping("/accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username")String username,
            @RequestParam("password")String password,
            HttpServletRequest request,
            HttpServletResponse response){

        // 认证，返回token，
        String token = authService.accredit(username,password);

        // 返回值检验
        if(StringUtils.isBlank(token)){
            // token 生成失败，返回未认证状态401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 将token保存在cookie中
        /**
         * cookie写入会遇到的问题
         *      1、nginx转发默认不会携带域名信息
         *      需要向请求的路径配置  peoxy_set_header Host $host
         *
         *      2、zuul默认会不携带头信息
         *      add_host_header:true
         *
         *      3、zuul默认会在响应前过滤掉cookie头信息"Cookie", "Set-Cookie", "Authorization"
         *      配置
         *      sensitive-headers:      为空即可
         *
         */
        CookieUtils.setCookie(request,response,
                jwtProperties.getCookieName(),  // cookie属性
                token,
                jwtProperties.getExpire()*60);     // 过期时间


        return ResponseEntity.ok(null);
    }
}
