package com.gms.service.jxc.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gms.dao.repository.CouponRepository;
import com.gms.entity.jxc.Coupon;
import com.gms.service.jxc.CouponService;

/**
 * 优惠券Service实现类
 * 
 * @author jxc
 *
 */
@Service("CouponService")
public class CouponServiceImpl implements CouponService {

	@Resource
	private CouponRepository couponRepository;

	@Override
	public void save(Coupon coupon) {
		couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findCouponAll() {
		return couponRepository.findAll();
	}

	@Override
	public List<Coupon> findCouponByStatus(Integer status) {
		return couponRepository.findCouponByStatus(status);
	}

}
