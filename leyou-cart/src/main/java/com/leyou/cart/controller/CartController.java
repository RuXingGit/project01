package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author RuXing
 * @create 2020-04-05 11:25
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    // 删除
    @DeleteMapping("/{skuId}")
    public ResponseEntity<String> deleteCart(
            @PathVariable("skuId")Long skuId){
        cartService.deleteCart(skuId);
        return ResponseEntity.ok("成功");
    }

    // 修改购物车商品购买数量
    @PutMapping
    public ResponseEntity<String> updateCart(@RequestBody Cart cart){
        cartService.updateCart(cart);
        return ResponseEntity.ok("成功");
    }

    // 查询购物车
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts = cartService.queryCarts();
        if(CollectionUtils.isEmpty(carts)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    // 添加购物车
    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
