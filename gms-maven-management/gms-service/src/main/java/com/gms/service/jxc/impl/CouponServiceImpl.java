package com.gms.service.jxc.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gms.dao.repository.CouponGoodsRepository;
import com.gms.dao.repository.CouponRepository;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponCode;
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
	public List<Coupon> findCouponAll(Integer shopId) {
		/*Pageable pageable = new PageRequest(current_page - 1, page_size);
		Page<Coupon> pageCoupon = couponRepository.findAll(
				new Specification<Coupon>() {
					@Override
					public Predicate toPredicate(Root<Coupon> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate = cb.conjunction();
						return predicate;
					}
				}, pageable);*/
		return couponRepository.findCouponAll(shopId);
	}

	@Override
	public List<Coupon> findCouponByStatus(Integer status,Integer shopId) {
		Date today = new Date();
		if (status == -1) {
			return couponRepository.findCouponAll(shopId);
		}
		else if (status == 1) {
			return couponRepository.findCouponBygt(today,shopId);
		} else if (status == 2) {
			return couponRepository.findCouponBybt(today,shopId);
		} else {
			return couponRepository.findCouponBylt(today,shopId);
		}

	}

	@Override
	public void saveCouponGoods(CouponGoods couponGoods) {
		couponGoodsRepository.save(couponGoods);
	}

	@Override
	public void deleteCoupon(Integer id,Integer shopId) {
		couponGoodsRepository.deleteCouponGoods(id, shopId);
		couponRepository.delete(id);
	}

	@Override
	public List<CouponGoods> findCouponListByGoodsId(Integer goodsId) {
		return couponGoodsRepository.findCouponListByGoodsId(goodsId);
	}

	@Override
	public Coupon findCouponById(Integer couponId) {
		return couponRepository.findOne(couponId);
	}

	@Override
	public List<CouponCode> findListByCouponId(Integer couponId) {
		return null;
	}

}