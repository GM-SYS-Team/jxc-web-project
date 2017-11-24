package com.gms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Shop;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.LogService;

@RestController
@RequestMapping("/admin/coupon")
public class CouponController extends BaseController {
	@Resource
	private LogService logService;
	@Autowired
	private CouponService couponService;

	/**
	 * @throws ParseException
	 * @Title: save
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param coupon
	 * @param @param request
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return Map<String,Object> 返回类型
	 * @throws
	 */
	@PostMapping("/save")
	public Map<String, Object> save(HttpServletRequest request)
			throws ParseException {
		String[] goodsIds = request.getParameter("goodsIds").split(",");
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Map<String, Object> resultMap = new HashMap<>();
		Coupon coupon = new Coupon();
		coupon.setCouponName(request.getParameter("couponName"));
		coupon.setTotalCount(Integer.parseInt(request
				.getParameter("totalCount")));
		coupon.setRemainCount(Integer.parseInt(request
				.getParameter("totalCount")));
		coupon.setCouponInfo(request.getParameter("couponIntro"));
		coupon.setCouponCount(request.getParameter("couponCount"));
		coupon.setCouponAmount(Double.parseDouble(request
				.getParameter("couponAmount")));
		String[] expiryDate = request.getParameter("expiryDate").split("-");
		coupon.setExpiryDateStart(sdf.parse(expiryDate[0].trim()));
		coupon.setExpiryDateStop(sdf.parse(expiryDate[1].trim()));
		coupon.setShop(shop);
		couponService.save(coupon);
		for (String goodsId : goodsIds) {
			CouponGoods couponGoods = new CouponGoods();
			couponGoods.setCouponId(coupon.getId());
			couponGoods.setShopId(shop.getId());
			couponGoods.setGoodId(Integer.parseInt(goodsId));
			couponService.saveCouponGoods(couponGoods);
		}
		resultMap.put("success", true);
		return resultMap;
	}

	@PostMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam(value = "id", required = true) Integer id,
			HttpServletRequest request) throws Exception {
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);
		couponService.deleteCoupon(id, shop.getId());
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		logService.save(new Log(Log.SEARCH_ACTION, "删除优惠券-->id:" + id)); // 写入日志
		return resultMap;
	}

	@PostMapping("/list")
	public Map<String, Object> list(
			@RequestParam(value = "num", required = true) Integer num,
			HttpServletRequest request) throws Exception {
		List<Coupon> couponList = new ArrayList<Coupon>();
		if (num == 0) {
			couponList = couponService.findCouponAll();
		} else {
			/* 当前登录的店铺 */
			Shop shop = getCurrentShop(request);
			couponList = couponService.findCouponByStatus(num, shop.getId());
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("couponList", couponList);
		resultMap.put("size", couponList == null ? 0 : couponList.size());
		logService.save(new Log(Log.SEARCH_ACTION, "查询优惠券信息")); // 写入日志
		return resultMap;
	}
}
