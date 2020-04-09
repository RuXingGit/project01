package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author RuXing
 * @create 2020-03-26 15:29
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.leyou.item.mapper")
public class LeyouApplication_service {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_service.class,args);
    }
}
