package com.gms.service.jxc.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gms.dao.repository.CouponCodeRepository;
import com.gms.entity.jxc.CouponCode;
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
	public List<CouponCode> queryCouponCodeList(Integer shopId, Date receiveDate) {
		return couponCodeRepository.queryCouponCodeByReTime(shopId, receiveDate);
	}


}