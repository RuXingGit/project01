package com.leyou.upload.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author RuXing
 * @create 2020-03-28 12:28
 */
public class CorsConfiguration {
    @Bean
    public CorsFilter corsFilter(){
        // cors配置对象
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        // 允许跨域访问的域名
        configuration.addAllowedOrigin("http://update.leyou.com");
        // 允许携带cookie，配置这个必须写确定的域名，false的话域名可以用*
        configuration.setAllowCredentials(true);
        // 允许跨域的请求方式,所有的请求方法
        configuration.addAllowedMethod("*");
        // 允许所有的头信息
        configuration.addAllowedHeader("*");



        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        // 创建cors过滤器
        CorsFilter corsFilter = new CorsFilter(source);
        return corsFilter;
    }
}
