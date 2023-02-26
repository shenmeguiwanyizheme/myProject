package com.example.administrator.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class MVCConfiguration implements WebMvcConfigurer {
    private ApplicationArguments applicationArguments;

    private MVCConfiguration(ApplicationArguments arguments) {
        this.applicationArguments = arguments;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserAuthorityResolver(applicationArguments));
    }
}
