package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author RuXing
 * @create 2020-03-26 14:53
 */
@SpringBootApplication
@EnableEurekaServer
public class LeyouApplication_eureka {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_eureka.class,args);
    }
}
