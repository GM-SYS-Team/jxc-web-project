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

import com.gms.dao.repository.CouponGoodsRepository;
import com.gms.dao.repository.CouponRepository;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Shop;
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
	public List<Coupon> findCouponAll(Shop shop) {
		if (shop != null) {
			return couponRepository.findCouponAll(shop.getId());
		} else {
			return couponRepository.findCouponAllAdmin();
		}

	}

	@Override
	public List<Coupon> findCouponByStatus(Integer status, Shop shop) {
		Date today = new Date();
		if (shop != null) {
			if (status == 0) {
				return couponRepository.findCouponAll(shop.getId());
			} else if (status == 1) {
				return couponRepository.findCouponBygt(today, shop.getId());
			} else if (status == 2) {
				return couponRepository.findCouponBybt(today, shop.getId());
			} else {
				return couponRepository.findCouponBylt(today, shop.getId());
			}
		} else {
			if (status == 0) {
				return couponRepository.findCouponAllAdmin();
			} else if (status == 1) {
				return couponRepository.findCouponBygtAdmin(today);
			} else if (status == 2) {
				return couponRepository.findCouponBybtAdmin(today);
			} else {
				return couponRepository.findCouponByltAdmin(today);
			}
		}

	}

	@Override
	public void saveCouponGoods(CouponGoods couponGoods) {
		couponGoodsRepository.save(couponGoods);
	}

	@Override
	public void deleteCoupon(Integer id, Integer shopId) {
		couponGoodsRepository.deleteCouponGoods(id, shopId);
		couponRepository.deleteCoupon(id);
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

	@Override
	public List<Coupon> list(Coupon coupon, Integer page, Integer pageSize,
			Direction direction, Integer state, String... properties) {
		Pageable pageable = new PageRequest(page - 1, pageSize, direction,
				properties);
		Page<Coupon> pageCoupon = couponRepository.findAll(
				new Specification<Coupon>() {
					@Override
					public Predicate toPredicate(Root<Coupon> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						Predicate predicate = cb.conjunction();
						Date currentDate = new Date();
						if (coupon != null) {
							if (coupon.getShopId() != null
									&& coupon.getShopId().intValue() > 0) {// 默认等于零
								predicate.getExpressions().add(
										cb.equal(root.get("shopId"),
												coupon.getShopId()));
							}
						}
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
		return pageCoupon.getContent();
	}

	@Override
	public Long listCount(Coupon coupon, Integer state) {
		Long count = couponRepository.count(new Specification<Coupon>() {

			@Override
			public Predicate toPredicate(Root<Coupon> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				Date currentDate = new Date();
				if (coupon != null) {
					if (coupon.getShopId() != null
							&& coupon.getShopId().intValue() > 0) {// 默认等于零
						predicate.getExpressions()
								.add(cb.equal(root.get("shopId"),
										coupon.getShopId()));
					}
				}
				if (state == 0) {
					// 获取商铺所有
				} else if (state == 1) {
					predicate.getExpressions().add(
							cb.greaterThan(root.get("expiryDateStart"),
									currentDate));
				} else if (state == 2) {
					predicate.getExpressions().add(
							cb.lessThanOrEqualTo(root.get("expiryDateStart"),
									currentDate));
					predicate.getExpressions().add(
							cb.greaterThanOrEqualTo(root.get("expiryDateStop"),
									currentDate));
				} else {
					predicate.getExpressions()
							.add(cb.lessThan(root.get("expiryDateStop"),
									currentDate));
				}
				return predicate;
			}
		});
		return count;
	}

}