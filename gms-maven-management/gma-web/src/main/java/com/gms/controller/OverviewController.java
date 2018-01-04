package com.gms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.CouponCodeService;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.LogService;
import com.gms.util.DateUtil;

@RestController
@RequestMapping("/admin/overview")
public class OverviewController extends BaseController {
	@Resource
	private LogService logService;
	@Autowired
	private CouponService couponService;

	@Autowired
	private ImageServerProperties imageServerProperties;

	@Autowired
	private CouponCodeService couponCodeService;

	@PostMapping("/map")
	public Map<String, Object> getContent(HttpServletRequest request)
			throws ParseException {
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);

		/* 查询当前店铺优惠券量 */
		int totalCount = couponService.findCouponAll(shop).size();
		int before_date_count = couponService.findCouponByStatus(1, shop)
				.size();
		int between_date_count = couponService.findCouponByStatus(2, shop)
				.size();
		int out_date_count = couponService.findCouponByStatus(3, shop).size();

		/* 优惠券领取状况 */
		Date today = new Date();
		Date dayBeforeSeven = DateUtil.getDateBeforeWeek(today);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dateList = DateUtil.getTwoDay(sdf.format(today),
				sdf.format(dayBeforeSeven), false);
		List<CouponCode> codeList = new ArrayList<CouponCode>();
		if(shop ==null){
			codeList = couponCodeService.queryCouponCodeAdmin(sdf.format(dayBeforeSeven));
		}else{
			codeList = couponCodeService.queryCouponCodeList(
					shop, sdf.format(dayBeforeSeven));
		}
		int[] amountArr = new int[7];
		String[] dateArr = new String[7];
		for (int i = 0; i < dateList.size(); i++) {
			dateArr[dateList.size() - 1 - i] = dateList.get(i);
			if (codeList.size() > 0) {
				for(CouponCode code:codeList){
					if(sdf.format(code.getReceiveTime()).equals(dateArr[dateList.size() - 1 - i])){
						amountArr[dateList.size() - 1 - i] += 1;
					}
				}
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("shop", shop);
		resultMap.put("totalCount", totalCount + "张");
		resultMap.put("before_date_count", before_date_count + "张");
		resultMap.put("between_date_count", between_date_count + "张");
		resultMap.put("out_date_count", out_date_count + "张");

		resultMap.put("dateArr", dateArr);
		resultMap.put("amountArr", amountArr);

		return resultMap;
	}

	@PostMapping("/user")
	public String getUser(HttpServletRequest request) throws ParseException {
		User user = getCurrentUser(request);
		return user.getUserName();
	}
}