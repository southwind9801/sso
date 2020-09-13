package com.southwind.configuration;

import com.southwind.interceptor.TaobaoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要拦截的路径，/**表示需要拦截所有请求
        String[] addPathPatterns = {"/taobao"};
        //注册一个拦截器
        registry.addInterceptor(new TaobaoInterceptor()).addPathPatterns(addPathPatterns);
    }
}
