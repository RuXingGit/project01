package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author RuXing
 * @create 2020-03-27 18:16
 */
@SpringBootApplication
@EnableEurekaClient
public class LeyouApplication_upload {
    public static void main(String[] args) {
        SpringApplication.run(LeyouApplication_upload.class,args);
    }
}
