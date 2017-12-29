package com.gms;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class JxcApplication extends SpringBootServletInitializer{
	private static Logger logger = LogManager.getLogger(JxcApplication.class);
	//
	public static void main(String[] args) {
		SpringApplication.run(JxcApplication.class, args);
		logger.info("info");
		logger.debug("debug");
		logger.error("error");
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JxcApplication.class);
	}
}
