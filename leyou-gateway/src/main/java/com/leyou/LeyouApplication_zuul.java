package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

/**
 * @author RuXing
 * @create 2020-03-26 15:02
 */
@SpringBootApplication
// 开启zuul代理
@EnableZuulProxy
// 微服务客户端
@EnableDiscoveryClient
public class LeyouApplication_zuul {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_zuul.class,args);
    }
}
