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
	private int modelSize;//单元模块大小5(像素)
	private int qzsize;//空白区16像素
	private String type;//文件后缀
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
	@Override
	public String toString() {
		return "QuickMarkProperties [rows=" + rows + ", cols=" + cols
				+ ", modelSize=" + modelSize + ", qzsize=" + qzsize + ", type="
				+ type + "]";
	}
	
	
}
