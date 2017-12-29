package com.gms.service.jxc.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
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
	public Page<CouponCode> list(Integer userId, Integer state, Integer page, Integer pageSize,
			Direction direction, String... properties) {
		Pageable pageable = new PageRequest(page - 1, pageSize, direction,
				properties);
		Page<CouponCode> pageCouponCode = couponCodeRepository.findAll(
				new Specification<CouponCode>() {
					@Override
					public Predicate toPredicate(Root<CouponCode> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate = cb.conjunction();
						Date currentDate = new Date();
						predicate.getExpressions().add(
								cb.equal(root.get("userId"),
										userId));
						if (state == 0) {
							// 获取商铺所有
						} else if (state == 1) {
							predicate.getExpressions().add(
									cb.greaterThan(root.get("expiryDateStart"),
											currentDate));
						} else if (state == 2) {
							predicate.getExpressions().add(
									cb.lessThanOrEqualTo(
											root.get("expiryDateStart"),
											currentDate));
							predicate.getExpressions().add(
									cb.greaterThanOrEqualTo(
											root.get("expiryDateStop"),
											currentDate));
						} else {
							predicate.getExpressions().add(
									cb.lessThan(root.get("expiryDateStop"),
											currentDate));
						}
						return predicate;
					}
				}, pageable);
		return pageCouponCode;
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