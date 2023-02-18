package com.example.user.config;

import com.example.user.Inteceptor.ErrorInterceptor;
import com.example.user.Inteceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    LoginInterceptor loginInterceptor;
    @Resource
    ErrorInterceptor errorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/error");
        registry.addInterceptor(errorInterceptor).addPathPatterns("/error");
        //实践证明，不是先匹配url路径是否存在，而是先被拦截器拦截,如果拦截器验证不通过，直接不匹配
        //并且，如果两个拦截器都拦截此url，那么按照代码书写顺序依次拦截
    }
}
