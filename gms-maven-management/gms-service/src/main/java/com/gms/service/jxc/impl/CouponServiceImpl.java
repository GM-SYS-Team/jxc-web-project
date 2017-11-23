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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.gms.dao.repository.CouponGoodsRepository;
import com.gms.dao.repository.CouponRepository;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponGoods;
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

	@Resource
	private CouponGoodsRepository couponGoodsRepository;

	@Override
	public void save(Coupon coupon) {
		couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findCouponAll(Integer current_page, Integer page_size) {
		Pageable pageable = new PageRequest(current_page - 1, page_size);
		Page<Coupon> pageCoupon = couponRepository.findAll(
				new Specification<Coupon>() {
					@Override
					public Predicate toPredicate(Root<Coupon> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate = cb.conjunction();
						return predicate;
					}
				}, pageable);
		return pageCoupon.getContent();
	}

	@Override
	public List<Coupon> findCouponByStatus(Integer status,
			Integer current_page, Integer page_size) {
		Date today = new Date();
		if (status == 1) {
			return couponRepository.findCouponBygt(today);
		} else if (status == 2) {
			return couponRepository.findCouponBybt(today);
		} else {
			return couponRepository.findCouponBylt(today);
		}
	}

	@Override
	public void saveCouponGoods(CouponGoods couponGoods) {
		couponGoodsRepository.save(couponGoods);
	}

}
