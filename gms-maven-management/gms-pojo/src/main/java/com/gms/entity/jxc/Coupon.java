package com.gms.entity.jxc;

import java.util.Date;

import javax.persistence.*;
/**
 * @author zhoutianqi
 * @className Coupon.java
 * @date 2017年11月16日 下午5:24:22
 * @description 优惠券
 */
@Entity
@Table(name = "t_coupon")
public class Coupon {

	@Id
	@GeneratedValue
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "shopId")
	private Shop shop; // 商品类别

	/**
	 * 优惠券批次号
	 */
	@Column(length = 11)
	private Integer totalCount;
	/**
	 * 优惠券剩余库存
	 */
	@Column(length = 11)
	private Integer remainCount;
	
	/*优惠券title*/
	@Column(length = 50)
	private String couponName;
	
	/*优惠券说明*/
	@Column(length = 500)
	private String couponInfo;

	/**
	 * 领取次数（每人限领）
	 */
	@Column(length = 45)
	private String couponCount;

	/**
	 * 有效开始时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDateStart;

	/**
	 * 有效结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiryDateStop;

	private Double minAmount;
	
	private Double maxAmount;
	
	private Double couponAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}


	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getRemainCount() {
		return remainCount;
	}

	public void setRemainCount(Integer remainCount) {
		this.remainCount = remainCount;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponInfo() {
		return couponInfo;
	}

	public void setCouponInfo(String couponInfo) {
		this.couponInfo = couponInfo;
	}

	public String getCouponCount() {
		return couponCount;
	}

	public void setCouponCount(String couponCount) {
		this.couponCount = couponCount;
	}

	public Date getExpiryDateStart() {
		return expiryDateStart;
	}

	public void setExpiryDateStart(Date expiryDateStart) {
		this.expiryDateStart = expiryDateStart;
	}

	public Date getExpiryDateStop() {
		return expiryDateStop;
	}

	public void setExpiryDateStop(Date expiryDateStop) {
		this.expiryDateStop = expiryDateStop;
	}

	public Double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(Double minAmount) {
		this.minAmount = minAmount;
	}

	public Double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(Double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public Double getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}
}