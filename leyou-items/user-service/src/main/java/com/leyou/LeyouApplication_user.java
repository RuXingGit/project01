package com.leyou;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author RuXing
 * @create 2020-04-03 15:23
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.leyou.user.mapper")
public class LeyouApplication_user {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_user.class,args);
    }
}
