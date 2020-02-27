package com.casic.cloud.hyperloop.core.config;

import com.casic.cloud.hyperloop.common.model.result.ApiErrorCode;
import com.casic.cloud.hyperloop.common.model.result.ApiResult;
import com.casic.cloud.hyperloop.core.interceptor.LoginInterceptor;
import com.casic.cloud.hyperloop.core.resolver.WithAccessMethodArgumentResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 以此方式注册拦截器，方便控制拦截规则
 * @Author: AnglIng
 * @Date: 2020/2/25 14:37
 * @version: V1.0
 */
@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private WithAccessMethodArgumentResolver withAccessMethodArgumentResolver;

    @Value("${safelogin.url.patterns}")
    private String patterns;

    @Value("${safelogin.url.exclude-patterns}")
    private String excludePatterns;


    /**
     * 注册拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns(patterns.split(","))
                .excludePathPatterns(excludePatterns.split(","));
    }

    /**
     * 注册自定义参数解析器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(withAccessMethodArgumentResolver);
    }

}
