package com.example.administrator.config;

import com.example.administrator.interceptor.CheckLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MVCInterceptorConfig implements WebMvcConfigurer {
    @Resource
    CheckLoginInterceptor checkLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //  *表示所有但不包括子目录
        //  /当前目录出了jsp的url   /*表示当前文件下包括jsp的所有文件但不包括子目录 /**则包括了子目录
        registry.addInterceptor(checkLoginInterceptor).addPathPatterns("/**");

    }
}
