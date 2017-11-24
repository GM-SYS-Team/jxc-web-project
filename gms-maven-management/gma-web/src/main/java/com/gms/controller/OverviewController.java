package com.gms.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Shop;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.LogService;

@RestController
@RequestMapping("/admin/overview")
public class OverviewController extends BaseController {
	@Resource
	private LogService logService;
	@Autowired
	private CouponService couponService;

	@PostMapping("/map")
	public Map<String, Object> save(HttpServletRequest request)
			throws ParseException {
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);

		/* 查询当前店铺优惠券总量 */
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("shop", shop);
		return resultMap;
	}
}
