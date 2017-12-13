package com.gms.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="image.quickmark")
public class QuickMarkProperties {
	/**
	 * @author zhoutianqi
	 * @className QuickMarkProperties.java
	 * @date 2017年12月4日 下午2:48:15
	 * @description 
	 */
	private int rows;
	private int cols;
	private int modelSize;
	private int qzsize;
	private String type;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getCols() {
		return cols;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public int getModelSize() {
		return modelSize;
	}
	public void setModelSize(int modelSize) {
		this.modelSize = modelSize;
	}
	public int getQzsize() {
		return qzsize;
	}
	public void setQzsize(int qzsize) {
		this.qzsize = qzsize;
	}
	
	
}
