package com.gms.conf;

import java.util.List;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableConfigurationProperties({ImageServerProperties.class})
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

	@Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		//exceptionResolvers.add(new MyExceptionHandler());
	}

}