package com.leyou.user.service;


import com.leyou.common.utils.Constants;
import com.leyou.common.utils.NumberUtils;
import com.leyou.item.pojo.User;
import com.leyou.user.mapper.UserMapper;
import com.leyou.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author RuXing
 * @create 2020-04-03 10:28
 */
@Service
public class UserService  {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 手机号和用户名验重

    public Boolean check(String data, Integer type) {
        if(StringUtils.isBlank(data)){
            return null;
        }
        User user = new User();
        // type 1:用户名，2:手机号
        if(type==1){
            // 根据用户名查询
            user.setUsername(data);
        }else if(type==2){
            // 根据手机号码查询
            user.setPhone(data);
        }else{
            // 数据不合法
            return null;
        }
        // 如果返回结果是0就说明没有重复
        return userMapper.selectCount(user)==0;
    }

    // 向rabbitmq手机号码，调用发送短信的微服务发送短信

    public void sendVerifyCode(String phone) {
        // 参数检验
        if(StringUtils.isBlank(phone)){
            return;
        }
        // 生成验证码
        String code = NumberUtils.generateCode(6);

        Map<String,String> msg =new HashMap<>();
        msg.put("phone",phone);
        msg.put("code",code);
        // 发送消息到rabbitmq
        amqpTemplate.convertAndSend("LEYOU.SMS.EXCHANGE","verifyCode.sms",msg);

        // 将验证码保存到redis
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(Constants.PHONE_PREFIX+phone,code,5, TimeUnit.MINUTES);
    }

    // 新增用户

    public Boolean register(User user, String code) {
        // 校验验证码
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String verifyKey = Constants.PHONE_PREFIX + user.getPhone();
        String verifyCode = ops.get(verifyKey);

        if(!StringUtils.equals(code,verifyCode)){
            return false;
        }
        // 生成盐值
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 盐值加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 新增用户
        user.setId(null);
        user.setCreated(new Date());
        userMapper.insertSelective(user);

        // 删除验证码
        stringRedisTemplate.delete(verifyKey);
        return true;
    }

    // 用户登录

    public User query(String username, String password) {
        // 参数验证
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return null;
        }
        // 查询用户
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);

        if(user==null){
            return null;
        }

        // 密码加密
        password = CodecUtils.md5Hex(password, user.getSalt());

        // 密码比较
        if(StringUtils.equals(password,user.getPassword())){
            return user;
        }

        return null;
    }
}
