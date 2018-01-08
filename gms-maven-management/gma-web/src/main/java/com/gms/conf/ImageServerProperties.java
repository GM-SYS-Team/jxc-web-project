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
	private String quickMarkAction;
	private String realQuickMarkAction;
	public String getQuickMarkAction() {
		return quickMarkAction;
	}
	public void setQuickMarkAction(String quickMarkAction) {
		this.quickMarkAction = quickMarkAction;
	}
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
	public String getRealQuickMarkAction() {
		return realQuickMarkAction;
	}
	public void setRealQuickMarkAction(String realQuickMarkAction) {
		this.realQuickMarkAction = realQuickMarkAction;
	}
	
}
