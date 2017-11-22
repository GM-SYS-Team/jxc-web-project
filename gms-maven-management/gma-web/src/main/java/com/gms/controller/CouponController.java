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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.Log;
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
	@RequestMapping("/save")
	public Map<String, Object> save(HttpServletRequest request)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Map<String, Object> resultMap = new HashMap<>();
		Coupon coupon = new Coupon();
		coupon.setCoupon_title(request.getParameter("coupon_title"));
		coupon.setBatchNum(Integer.parseInt(request.getParameter("batchNum")));
		coupon.setCoupon_intro(request.getParameter("coupon_intro"));
		coupon.setExpiryDateStart(sdf.parse(request
				.getParameter("expiryDateStart")));
		coupon.setExpiryDateStop(sdf.parse(request
				.getParameter("expiryDateStop")));
		coupon.setCouponCount(request.getParameter("couponCount"));
		couponService.save(coupon);
		resultMap.put("success", true);
		return resultMap;
	}

	@RequestMapping("/list")
	public Map<String, Object> list(
			@RequestParam(value = "num", required = true) Integer num,
			HttpServletRequest request) throws Exception {
		List<Coupon> shopList = new ArrayList<Coupon>();
		if (num == 0) {
			shopList = couponService.findCouponAll();
		} else {
			shopList = couponService.findCouponByStatus(num);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", shopList);
		logService.save(new Log(Log.SEARCH_ACTION, "查询优惠券信息")); // 写入日志
		return resultMap;
	}
}
