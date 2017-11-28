package com.gms.entity.jxc;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户实体
 * @author jxc 
 *
 */
@Entity
@Table(name="t_user")
public class User {

	/**
	 * 管理员
	 */
	public static final String ADMIN = "0";
	
	/**
	 * 店家
	 */
	public static final String SHOPER = "1";
	
	/**
	 * 用户
	 */
	public static final String CUSTOMER = "2";
	
	@Id
	@GeneratedValue
	private Integer id; // 编号
	
	
	
	@NotEmpty(message="请输入用户名！")
	@Column(length=50)
	private String userAccount; // 用户名
	
	@NotEmpty(message="请输入密码！")
	@Column(length=50)
	@JsonIgnore
	private String password; // 密码
	
	@Column(length=50)
	private String trueName; // 真实姓名
	
	@Column(length=200)
	private String shopName; // 商户名称
	
	@Column(length=50)
	private String nickName = "新用户";
	
	@Column(length=1000)
	private String remarks; // 备注
	
	@Column(length=20)
	private String phoneNum;
	
	@Column(length=500)
	private String address;
	
	@Column(length=100)
	private String district;
	
	@Column(length=1)
	private String userType;
	
	@Column(length=11)
    private Integer shopId; // 商户
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Column(length=64)
	private String uuid;
	
	@Transient
	private String roles; 
	
	@Column
	private String imgUrl; //用户头像
	
	@Column(length=11)
    private Integer currentLoginShopId;//上次访问的店铺
    

    public Integer getCurrentLoginShopId() {
		return currentLoginShopId;
	}

	public void setCurrentLoginShopId(Integer currentLoginShopId) {
		this.currentLoginShopId = currentLoginShopId;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}


	public String getRemarks() {
		return remarks;
	}


	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", password=" + password + ", trueName=" + trueName
				+ ", remarks=" + remarks + ", roles=" + roles + "]";
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void generateUUID(){
		this.uuid =  UUID.randomUUID().toString().replaceAll("-", "");
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}
	
	public User() {
		
	}
	
	public User(User user) {
		this.address = user.getAddress();
		this.createTime = user.getCreateTime();
		this.district = user.getDistrict();
		this.id = user.getId();
		this.imgUrl = user.getImgUrl();
		this.nickName = user.getNickName();
		this.phoneNum = user.getPhoneNum();
		this.password = user.getPassword();
		this.uuid = user.getUuid();
		this.remarks = user.getRemarks();
		this.userType = user.getUserType();
		this.userAccount = user.getUserAccount();
		this.updateTime = user.getUpdateTime();
	}
}