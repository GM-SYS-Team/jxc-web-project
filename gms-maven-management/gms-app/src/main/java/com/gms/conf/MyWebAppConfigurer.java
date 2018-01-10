package com.gms.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.gms.interceptor.CORSInterceptor;
import com.gms.interceptor.UserInterceptor;

@Configuration
@EnableConfigurationProperties({ImageServerProperties.class})
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

	@Bean
	public HandlerInterceptor getUserInterceptor() {
		return new UserInterceptor();
	}
	
	@Bean
	public HandlerInterceptor getCORSInterceptor() {
		return new CORSInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 多个拦截器组成一个拦截器链
		// addPathPatterns 用于添加拦截规则
		// excludePathPatterns 用户排除拦截
		registry.addInterceptor(getCORSInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(getUserInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

}