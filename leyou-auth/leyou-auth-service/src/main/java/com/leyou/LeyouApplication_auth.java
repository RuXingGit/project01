package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author RuXing
 * @create 2020-04-03 17:05
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
public class LeyouApplication_auth {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_auth.class,args);
    }
}
