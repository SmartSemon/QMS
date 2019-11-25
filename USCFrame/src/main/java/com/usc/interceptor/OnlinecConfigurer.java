package com.usc.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OnlinecConfigurer implements WebMvcConfigurer {
    @Autowired
    private OnlineInterceptor onlineInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(onlineInterceptor).addPathPatterns("/**").excludePathPatterns("/login",
                "/logout", "/src/**");
    }
}
