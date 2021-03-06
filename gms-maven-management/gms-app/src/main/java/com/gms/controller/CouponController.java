package com.gms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.CouponCodeService;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;

@Controller
@RequestMapping("/app/coupon")
@NeedAuth
public class CouponController extends BaseAppController {
	@Autowired
	private CouponService couponService;

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private CouponCodeService couponCodeService;

	/**
	 * 
	 * @Title: save
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param request
	 * @throws Exception
	 * @return Map<String,Object> 返回类型
	 * @throws MyException 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request) throws ParseException, MyException {
		User user = getUser();
		validateUser(user, User.SHOPER);
		Integer goodsId = Integer.parseInt(request.getParameter("goodsId"));
		/* 当前登录的店铺 */
		Integer shopId = Integer.parseInt(request.getParameter("shopId"));
		String couponName = request.getParameter("couponName");
		Integer totalCount = Integer.parseInt(request.getParameter("totalCount"));
		String couponInfo = request.getParameter("couponInfo");
		Double maxAmount = Double.parseDouble(request.getParameter("maxAmount"));
		Double minAmount = Double.parseDouble(request.getParameter("minAmount"));
		String expiryDateStart = request.getParameter("expiryDateStart");
		String expiryDateStop = request.getParameter("expiryDateStop");
		if (shopId == null || shopId <= 0) {
			return error("非法请求");
		}
		if (StringUtil.isEmpty(couponName)) {
			return error("请设置优惠券名称");
		}
		if (totalCount <= 0) {
			return error("请设置优惠券数量");
		}
		if (StringUtil.isEmpty(couponInfo)) {
			return error("请设置优惠券描述");
		}
		if (maxAmount <= 0 || minAmount <= 0) {
			return error("请设置优惠券金额");
		}
		if (goodsId == null || goodsId <= 0) {
			return error("请选择商品");
		}
		if (maxAmount > minAmount) {
			return error("优惠券最大金额不得低于优惠券最小金额");
		}
		if (StringUtil.isEmpty(expiryDateStart)) {
			return error("优惠券开始时间不能为空");
		}
		if (StringUtil.isEmpty(expiryDateStop)) {
			return error("优惠券结束时间不能为空");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Coupon coupon = new Coupon();
		coupon.setCouponName(couponName);
		coupon.setTotalCount(totalCount);
		coupon.setRemainCount(totalCount);
		coupon.setMaxAmount(maxAmount);
		coupon.setMinAmount(minAmount);
		try {
			coupon.setExpiryDateStart(sdf.parse(expiryDateStart));
			coupon.setExpiryDateStop(sdf.parse(expiryDateStop));
		} catch (Exception e) {
			return error("优惠券开始时间和结束时间参数错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		coupon.setShopId(shopId);
		couponService.save(coupon);
		CouponGoods couponGoods = new CouponGoods();
		couponGoods.setCouponId(coupon.getId());
		couponGoods.setShopId(shop.getId());
		couponGoods.setGoodId(goodsId);
		couponService.saveCouponGoods(couponGoods);
		return success(coupon);
	}

	/**
	 * 修改优惠券信息
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws MyException 
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public Map<String, Object> modify(HttpServletRequest request) throws ParseException, MyException {
		User user = getUser();
		validateUser(user, User.SHOPER);
		Integer goodsId = Integer.parseInt(request.getParameter("goodsId"));
		/* 当前登录的店铺 */
		Integer couponId = Integer.parseInt(request.getParameter("couponId"));
		Integer shopId = Integer.parseInt(request.getParameter("shopId"));
		String couponName = request.getParameter("couponName");
		Integer totalCount = Integer.parseInt(request.getParameter("totalCount"));
		String couponInfo = request.getParameter("couponInfo");
		Double maxAmount = Double.parseDouble(request.getParameter("maxAmount"));
		Double minAmount = Double.parseDouble(request.getParameter("minAmount"));
		String expiryDateStart = request.getParameter("expiryDateStart");
		String expiryDateStop = request.getParameter("expiryDateStop");
		if (shopId == null || shopId <= 0) {
			return error("非法请求");
		}
		if (StringUtil.isEmpty(couponName)) {
			return error("请设置优惠券名称");
		}
		if (couponName.length() > 50) {
			return error("优惠券名称长度不能超过50");
		}
		if (totalCount <= 0) {
			return error("请设置优惠券数量");
		}
		if (StringUtil.isEmpty(couponInfo)) {
			return error("请设置优惠券描述");
		}
		if (couponInfo.length() > 500) {
			return error("优惠券描述的长度不能超过500");
		}
		if (maxAmount <= 0 || minAmount <= 0) {
			return error("请设置优惠券金额");
		}
		if (goodsId == null || goodsId <= 0) {
			return error("请选择商品");
		}
		if (maxAmount > minAmount) {
			return error("优惠券最大金额不得低于优惠券最小金额");
		}
		if (StringUtil.isEmpty(expiryDateStart)) {
			return error("优惠券开始时间不能为空");
		}
		if (StringUtil.isEmpty(expiryDateStop)) {
			return error("优惠券结束时间不能为空");
		}
		Date startTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			startTime = sdf.parse(expiryDateStart);
			endTime = sdf.parse(expiryDateStop);
		} catch (Exception e) {
			return error("优惠券开始时间和结束时间参数错误");
		}

		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Coupon coupon = couponService.findCouponById(couponId);
		if (coupon == null) {
			return error("优惠券不存在");
		}
		if (coupon.getShopId() != shopId) {
			return error("优惠券不存在");
		}
		coupon.setCouponName(couponName);
		coupon.setTotalCount(totalCount);
		coupon.setRemainCount(totalCount);
		coupon.setMaxAmount(maxAmount);
		coupon.setMinAmount(minAmount);
		coupon.setExpiryDateStart(startTime);
		coupon.setExpiryDateStop(endTime);
		coupon.setShopId(shopId);
		couponService.save(coupon);
		return success(coupon);
	}

	/**
	 * 优惠券删除
	 * 
	 * @param couponId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer couponId, Integer shopId) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		if (couponId == null || couponId <= 0) {
			return error("优惠券id错误");
		}
		if (shopId == null || shopId <= 0) {
			return error("店铺id错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Coupon coupon = couponService.findCouponById(couponId);
		if (coupon == null) {
			return error("优惠券不存在");
		}
		if (coupon.getShopId() != shopId) {
			return error("优惠券不存在");
		}
		Date now = new Date();
		if (coupon.getExpiryDateStart().before(now) && coupon.getExpiryDateStop().after(now)) {
			return error("该优惠券正在发放，无法删除");
		}
		List<CouponCode> list = couponService.findListByCouponId(couponId);
		if (list != null && list.size() > 0) {
			return error("该优惠券已经发放，无法删除");
		}
		couponService.deleteCoupon(couponId, shopId);
		return success();
	}

	/**
	 * 
	 * 商家优惠券的设置
	 * 
	 * @param status
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> list(Integer status, Integer shopId) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		if (shopId == null || shopId <= 0) {
			return error("店铺id错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		List<Coupon> couponList = new ArrayList<Coupon>();
		/* 当前登录的店铺 */
		couponList = couponService.findCouponByStatus(status, shop);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("couponList", couponList);
		resultMap.put("size", couponList == null ? 0 : couponList.size());
		return success(resultMap);
	}

	/**
	 * 优惠券共享
	 * 
	 * @param couponId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/shareCoupon")
	@ResponseBody
	public Map<String, Object> shareCoupon(Integer couponId, Integer shopId) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		if (couponId == null || couponId <= 0) {
			return error("优惠券id错误");
		}
		if (shopId == null || shopId <= 0) {
			return error("店铺id错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Coupon coupon = couponService.findCouponById(couponId);
		if (coupon == null) {
			return error("优惠券不存在");
		}
		if (coupon.getShopId() != shopId) {
			return error("优惠券不存在");
		}
		coupon.setStatus(Coupon.STATUS_SHARE);
		couponService.save(coupon);
		return success();
	}
	
	/**
	 * 查询用户领取的优惠券
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/list")
	@ResponseBody
	public Map<String, Object> listUserCoupon(Integer status) throws Exception {
		User user = getUser();
		validateUser(user, User.CUSTOMER);
		List<CouponCode> list = couponCodeService.findListByUserId(user.getId(), status);
		return success(list);
	}
	
	
	/**
	 * 领券
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/receive")
	@ResponseBody
	public Map<String, Object> receiveCoupon(Integer couponId, Integer shopId) throws Exception {
		User user = getUser();
		validateUser(user, User.CUSTOMER);
		CouponCode couponCode = new CouponCode();
		synchronized (this) {
			Coupon coupon = couponService.findCouponById(couponId);
			if (coupon == null) {
				return error("优惠券不存在");
			}
			if (coupon.getShopId() != shopId) {
				return error("优惠券不存在");
			}
			Date now = new Date();
			if (DateUtil.compare_date(now, coupon.getExpiryDateStop()) == 1) {
				return error("优惠券已过期无法领取");
			}
			if (coupon.getRemainCount() <= 0) {
				return error("优惠券已发放完毕");
			}
			couponCode.setReceiveTime(now);
			couponCode.setCoupon(coupon);
			couponCode.setAmount((int) (Math.random()*(coupon.getMaxAmount() - coupon.getMinAmount()) + coupon.getMinAmount()));
			couponCode.setUserId(user.getId());
			couponCode.setIsUsed(Constant.COUPON_NOT_USED);
			couponCode.setExpiryDateStart(coupon.getExpiryDateStart());
			couponCode.setExpiryDateStop(coupon.getExpiryDateStop());
			coupon.setRemainCount(coupon.getRemainCount() - 1);
			couponService.save(coupon);
			couponCodeService.save(couponCode);
		}
		return success(couponCode);
	}
	
	/**
	 * 销券
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/sale")
	@ResponseBody
	public Map<String, Object> saleCoupon(Integer couponCodeId, Integer ownerId) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		CouponCode couponCode = couponCodeService.findCouponCodeById(couponCodeId, ownerId);
		if (couponCode == null) {
			return error("优惠券不存在");
		}
		Coupon coupon = couponCode.getCoupon();
		Shop shop = shopService.findById(coupon.getShopId());
		if (shop.getUserId() != user.getId()) {
			return error("优惠券不存在");
		}
		Date now = new Date();
		if (DateUtil.compare_date(now, couponCode.getExpiryDateStop()) == 1) {
			return error("优惠券已过期");
		}
		if (DateUtil.compare_date(couponCode.getExpiryDateStart(), now) == 1) {
			return error("该活动尚未开始");
		}
 		couponCode.setUsedTime(now);
		couponCode.setIsUsed(Constant.COUPON_USED);
		couponCodeService.save(couponCode);
		return success();
	}
	
	/**
	 * 查询用户领取的优惠券
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user")
	@ResponseBody
	public Map<String, Object> getCoupon(Integer couponId, Integer shopId) throws Exception {
		User user = getUser();
		validateUser(user, User.CUSTOMER);
		Coupon coupon = couponService.findCouponById(couponId);
		if (coupon == null) {
			return error("优惠券活动不存在");
		}
		if (coupon.getShopId() != shopId) {
			return error("优惠券活动不存在");
		}
		Date now = new Date();
		if (DateUtil.compare_date(now, coupon.getExpiryDateStop()) == 1) {
			return error("活动已结束");
		}
		Coupon randomCoupon = couponService.findRandomCoupon(now, coupon.getId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("coupon", coupon);
		map.put("shareCoupon", randomCoupon);
		return success(map);
	}
	
	/**
	 * 判断用户类型
	 * @param user
	 * @throws MyException 
	 */
	private void validateUser(User user, String userType) throws MyException {
		if (!userType.equals(user.getUserType())) {
			throw new MyException("非法请求");
		}
	}
}
