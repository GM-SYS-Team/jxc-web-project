package com.gms.entity.jxc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "t_app")
public class App {
	/**
	 * @author zhoutianqi
	 * @className app.java
	 * @date 2018年1月17日 下午3:49:01
	 * @description 
	 */
	@SuppressWarnings("unused")
	private final static String IOS = "ios";
	@SuppressWarnings("unused")
	private final static String ANDROID = "android";
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(length=50)
	private String appName;
	
	@Column(length=50)
	private String appVersion;
	
	@Column(length=50)
	private String appType;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Column(length=100)
	private String appUrl;
	
	@Column(length=200)
	private String remarks; // 备注
	
	@Transient
	private String btime; // 起始时间  搜索用到
	
	@Transient
	private String etime; // 结束时间  搜索用到

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getBtime() {
		return btime;
	}

	public void setBtime(String btime) {
		this.btime = btime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}
	
	
}
