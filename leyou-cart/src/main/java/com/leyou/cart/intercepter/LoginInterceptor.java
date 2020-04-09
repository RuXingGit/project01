package com.leyou.cart.intercepter;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.cart.config.LoginProperties;
import com.leyou.common.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RuXing
 * @create 2020-04-05 10:58
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private LoginProperties loginProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL =new ThreadLocal<>();

    // 解析token获取用户信息
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie token
        String token = CookieUtils.getCookieValue(request, loginProperties.getCookieName());
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, loginProperties.getPublicKey());
        if(user==null){
            return false;
        }

        // 将user信息保存到本地线程ThreadLocal,是属于线程的局部变量
        THREAD_LOCAL.set(user);

        return true;
    }
    // 释放ThreadLocal的内存占用，因为tomcat使用的是线程池
    // 不清除的话当这个线程回收后再次使用的时候，会出现脏数据
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空线程的局部变量
        THREAD_LOCAL.remove();
    }

    // 获取用户信息
    public static UserInfo getUserInfo(){
        UserInfo userInfo = THREAD_LOCAL.get();
        return userInfo;
    }
}
