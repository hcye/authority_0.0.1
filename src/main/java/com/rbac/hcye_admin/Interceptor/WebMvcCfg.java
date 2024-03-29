package com.rbac.hcye_admin.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcCfg implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //使用过滤器验证是否登录，放行静态资源和登录页面及请求
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/css/**","/font/**","/js/**","/layui/**","/layui_ext/**","/","/img/**","/index","/login.html");
        //重复提交过滤器，10秒内拦截重复提交请求
        registry.addInterceptor(new RepeatSubmitInterceptor()).addPathPatterns("/**");
    }
}
