package com.example.administrator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        //1. 添加 CORS配置信息
        CorsConfiguration config = new CorsConfiguration();
        //放行哪些原始域
        config.addAllowedOrigin("*");//所有的域名可以请求过来
        //是否发送 Cookie
        config.setAllowCredentials(true);//放行的域名允许携带cookie
        //放行哪些请求方式
        config.addAllowedMethod("*");//get post方法都允许跨域
        //放行哪些原始请求头部信息
        config.addAllowedHeader("*");//允许跨域的头部key名称
        //暴露哪些头部信息
        config.addExposedHeader("*");//哪些头部的key名称和value值可以被返回
        //2. 添加映射路径
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", config);//哪些请求路径可以跨域
        //3. 返回新的CorsFilter
        return new CorsFilter(corsConfigurationSource);
    }
}
