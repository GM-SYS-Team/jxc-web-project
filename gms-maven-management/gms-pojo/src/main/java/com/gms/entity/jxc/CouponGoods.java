package com.gms.entity.jxc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 商品实体
 * @author jxc 
 *
 */
@Entity
@Table(name="t_coupon_goods")
public class CouponGoods {

	@Id
	@GeneratedValue
	private Integer id; // 编号

	@Column(length=20)
	private Integer copponId; // 优惠券ID
	
	@Column(length=20)
	private Integer shopId; // 商铺ID
	

	@Column(length=20)
	private Integer goodId; // 商品ID
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCopponId() {
		return copponId;
	}

	public void setCopponId(Integer copponId) {
		this.copponId = copponId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getGoodId() {
		return goodId;
	}

	public void setGoodId(Integer goodId) {
		this.goodId = goodId;
	}

	
}
