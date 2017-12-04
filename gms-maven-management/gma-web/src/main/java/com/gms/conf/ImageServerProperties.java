package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.server")
public class ImageServerProperties {
	/**
	 * @author zhoutianqi
	 * @className ImageServerProperties.java
	 * @date 2017年12月4日 下午2:48:15
	 * @description 
	 */
	private String url;
	private String action;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
}
