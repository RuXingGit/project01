package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.intercepter.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @author RuXing
 * @create 2020-04-05 11:26
 */
@Service
public class CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    private static final String CART_PREFIX = "user:cart:";

    public void addCart(Cart cart) {
        if(cart==null){
            return;
        }
        // 获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        // 查询redis的购物车记录
        BoundHashOperations<String, Object, Object> hashOps
                = stringRedisTemplate.boundHashOps(CART_PREFIX + user.getId());
        // 判断商品是否已经存在，没有商品新增 有更新
        String cartKey = cart.getSkuId().toString();

        // 判断
        Boolean hasKey = hashOps.hasKey(cartKey);
        // 记录购买数量
        Integer num =cart.getNum();
        if(hasKey){
            // 获取redis中已经保存的购物车     进行更新
            String cartJson = hashOps.get(cartKey).toString();

            // 反序列化json
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 更新数量
            cart.setNum(cart.getNum() + num);
        }else{                                              // 新增购物车
            // 获取sku
            Sku sku = goodsClient.querySkuById(cart.getSkuId());
            if(sku==null){
                return;
            }
            // 设置cart属性
            cart.setUserId(user.getId());
            cart.setImage(sku.getImage());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
        }
        String serialize = JsonUtils.serialize(cart);
        // 将cart转化成为json保存到redis
        hashOps.put(cartKey, serialize);

    }

    // 查询购物车
    public List<Cart> queryCarts() {
        // 获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        // userKey
        String userKey = CART_PREFIX + user.getId();
        // 用户没有创建购物车时
        if(StringUtils.isEmpty(userKey)){
            return null;
        }
        // 保存hash的hash      返回的是map
        BoundHashOperations<String, Object, Object> hashOps
                = stringRedisTemplate.boundHashOps(userKey);
        // 获取所有hash的value
        List<Object> cartsJson = hashOps.values();
        // 判断是否为空
        if(CollectionUtils.isEmpty(cartsJson)){
            return null;
        }

        // 反序列化json
        List<Cart> carts = cartsJson.stream().map(cart -> {
            return JsonUtils.parse(cart.toString(), Cart.class);
        }).collect(Collectors.toList());

        return carts;
    }

    // 修改购买数量
    public void updateCart(Cart cart) {
        // 获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        // userKey
        String userKey = CART_PREFIX + user.getId();
        // 用户没有创建购物车时
        if(StringUtils.isEmpty(userKey)){
            return;
        }
        // 保存hash的hash      返回的是map
        BoundHashOperations<String, Object, Object> hashOps
                = stringRedisTemplate.boundHashOps(userKey);
        // 用来修改的键
        String cartKey = cart.getSkuId().toString();
        // 保存数量
        Integer num =cart.getNum();
        // 获取redis中保存的cart

        if(hashOps.get(cartKey)==null){
            return;
        }
        cart  = JsonUtils.parse(hashOps.get(cartKey).toString(), Cart.class);

        cart.setNum(num);
        // 更新
        hashOps.put(cartKey,JsonUtils.serialize(cart));
    }

    public void deleteCart(Long skuId) {
        // 获取用户信息
        UserInfo user = LoginInterceptor.getUserInfo();
        // userKey
        String userKey = CART_PREFIX + user.getId();
        // cartKey
        String cartKey = skuId.toString();
        // 用户没有创建购物车时
        if(StringUtils.isEmpty(userKey)){
            return;
        }
        // 保存hash的hash      返回的是map
        BoundHashOperations<String, Object, Object> hashOps
                = stringRedisTemplate.boundHashOps(userKey);
        // 删除
        hashOps.delete(cartKey);
    }
}
