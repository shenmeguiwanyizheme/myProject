package com.example.user.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebMVCConfiguration implements WebMvcConfigurer {
    private ApplicationArguments applicationArguments;

    public WebMVCConfiguration(ApplicationArguments applicationArguments) {
        this.applicationArguments = applicationArguments;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserAuthorityResolver(applicationArguments));
    }
}
