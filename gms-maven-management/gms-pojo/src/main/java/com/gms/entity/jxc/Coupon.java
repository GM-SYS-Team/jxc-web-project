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

	@Column
	private Integer shopId;
	
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

	@Column
	private Double minAmount;
	
	@Column
	private Double maxAmount;
	
	@Column
	private Double couponAmount;
	
	/**
	 * 优惠券状态 默认为0， 1-发放优惠券
	 */
	@Column
	private String status;
	
	@OneToOne
	@JoinColumn(name="id")
	private CouponGoods couponGoods; 
	
	/**
	 * 表示优惠券为共享优惠券
	 */
	public final static String STATUS_SHARE = "1";
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public CouponGoods getCouponGoods() {
		return couponGoods;
	}

	public void setCouponGoods(CouponGoods couponGoods) {
		this.couponGoods = couponGoods;
	}
}