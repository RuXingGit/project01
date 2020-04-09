package com.leyou.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author RuXing
 * @create 2020-03-31 14:34
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class LeyouApplication_search {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_search.class,args);
    }
}
