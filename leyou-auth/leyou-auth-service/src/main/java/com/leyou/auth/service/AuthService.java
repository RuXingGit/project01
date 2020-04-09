package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.item.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author RuXing
 * @create 2020-04-03 17:44
 */
@Service
public class AuthService {

    /**
     * 远程调用
     *      Feign       @EnableFeignClients     @FeignClient("xxx-service")
     *
     */
    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties jwtProperties;

    public String accredit(String username, String password) {
        // 根据用户名和密码查询
        User user = userClient.query(username, password);
        if(user==null){
            return null;
        }
        try {
            // 生成rsa加密后的jwt
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(user,userInfo);

            // 生成token
            return  JwtUtils.generateToken(userInfo,    // 根据用户信息生成jwt证书
                    jwtProperties.getPrivateKey(),      // 使用私钥对jwt进行加密
                    jwtProperties.getExpire());         // token证书有效时间

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
