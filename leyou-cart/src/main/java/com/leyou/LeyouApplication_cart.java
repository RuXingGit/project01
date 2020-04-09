package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author RuXing
 * @create 2020-04-04 12:06
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class LeyouApplication_cart {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_cart.class,args);
    }
}
