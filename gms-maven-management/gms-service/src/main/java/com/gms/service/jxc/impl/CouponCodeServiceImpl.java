package com.gms.service.jxc.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gms.dao.repository.CouponCodeRepository;
import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.Shop;
import com.gms.service.jxc.CouponCodeService;

/**
 * 领取优惠券Service实现类
 * 
 * @author jxc
 *
 */
@Service("CouponCodeService")
public class CouponCodeServiceImpl implements CouponCodeService {

	@Resource
	private CouponCodeRepository couponCodeRepository;

	@Override
	public List<CouponCode> queryCouponCodeList(Shop shop, Date receiveDate) {
		if (shop != null) {
			return couponCodeRepository.queryCouponCodeByReTime(shop.getId(), receiveDate);
		} else {
			return couponCodeRepository.queryCouponCodeByReTimeAdmin(receiveDate);
		}
	}

	@Override
	public List<CouponCode> findListByUserId(Integer userId, Integer status) {
		Date today = new Date();
		//所有
		if (status == 0) {
			return couponCodeRepository.findCouponAll(userId);
		} 
		//已过期
		else if (status == 1) {
			return couponCodeRepository.findCouponBygtNow(userId, today);
		} 
		//未使用
		else if (status == 2) {
			return couponCodeRepository.findNotUseCoupon(userId, today);
		} 
		//已使用
		else {
			return couponCodeRepository.findUsedCoupon(userId);
		}
	}

	@Override
	public void save(CouponCode couponCode) {
		couponCodeRepository.save(couponCode);
	}

	@Override
	public CouponCode findCouponCodeById(Integer couponCodeId,  Integer ownerId) {
		return couponCodeRepository.findCouponCodeById(couponCodeId, ownerId);
	}

}