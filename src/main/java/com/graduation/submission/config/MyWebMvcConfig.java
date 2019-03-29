package com.graduation.submission.config;

import com.graduation.submission.interceptor.UserActionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName MyWebMvcConfig
 * @Description TODO
 * @Author LJL
 * @Date 2019/3/20 23:59
 * @Version 1.0
 **/
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {


    /**
     *
     * @return
     */
    @Bean
    public UserActionInterceptor userActionInterceptor(){
        return new UserActionInterceptor();
    }
    /**
     * 添加自定义静态资源映射路径和静态资源存放路径
     */
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/js/**").addResourceLocations("/js/");
		registry.addResourceHandler("/layui/**")
				.addResourceLocations("/layui/");
		super.addResourceHandlers(registry);
	}*/

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 路径根据后期项目的扩展，进行调整
        registry.addInterceptor(userActionInterceptor())
                .addPathPatterns("/user/**", "/auth/**")
                .excludePathPatterns("/user/sendMsg", "/user/login","/user/register","/user/forget");
    }
}
