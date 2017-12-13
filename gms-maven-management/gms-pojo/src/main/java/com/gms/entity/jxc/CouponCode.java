package com.gms.entity.jxc;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author zhoutianqi
 * @className CouponCode.java
 * @date 2017年11月16日 下午5:24:22
 * @description 优惠券
 */
@Entity
@Table(name = "t_coupon_code")
public class CouponCode {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 优惠券
     */
    @ManyToOne
	@JoinColumn(name="couponId")
	private Coupon coupon; // 优惠券
	
    /**
     * 用户ID
     */
    @Column(length=11)
    private Integer userId;

    /**
     * 未使用：0  已使用：1
     */
    @Column(length=1)
    private String isUsed;
    
    /**
     * 优惠卷金额
     */
    private Integer amount;

    
    /* 领取时间*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveTime;
    
    /*使用时间*/
    @Temporal(TemporalType.TIMESTAMP)
    private Date usedTime;
    

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
    
    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }
	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取未使用：0  已使用：1
     *
     * @return is_used - 未使用：0  已使用：1
     */
    public String getIsUsed() {
        return isUsed;
    }

    /**
     * 设置未使用：0  已使用：1
     *
     * @param isUsed 未使用：0  已使用：1
     */
    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
    

	public Date getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
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
}