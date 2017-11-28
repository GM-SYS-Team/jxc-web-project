package com.gms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.CouponCodeService;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.DamageListService;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.PurchaseListService;
import com.gms.service.jxc.SaleListService;
import com.gms.util.Constant;
import com.gms.util.DateUtil;

@RestController
@RequestMapping("/admin/overview")
public class OverviewController extends BaseController {
	@Resource
	private LogService logService;
	@Autowired
	private CouponService couponService;

	@Autowired
	private PurchaseListService purchaseListService;
	@Autowired
	private DamageListService damageListService;
	@Autowired
	private SaleListService saleListService;

	@Autowired
	private CouponCodeService couponCodeService;

	@PostMapping("/map")
	public Map<String, Object> save(HttpServletRequest request)
			throws ParseException {
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);
		
		/* 查询当前店铺优惠券量 */
		int totalCount = couponService.findCouponAll(shop.getId()).size();
		int before_date_count = couponService.findCouponByStatus(1,
				shop.getId()).size();
		int between_date_count = couponService.findCouponByStatus(2,
				shop.getId()).size();
		int out_date_count = couponService.findCouponByStatus(3, shop.getId())
				.size();

		// 已支付进货单数量
		int jin_num1 = purchaseListService.getPurchasePayedListCount(
				shop.getId(), Constant.ORDER_PAYED_STATE);
		// 未支付进货单数量
		int jin_num2 = purchaseListService.getPurchasePayedListCount(
				shop.getId(), Constant.ORDER_NO_PAYED_STATE);
		int sale_num1 = saleListService.getSaleOrderPayedListCount(
				shop.getId(), Constant.ORDER_PAYED_STATE);
		int sale_num2 = saleListService.getSaleOrderPayedListCount(
				shop.getId(), Constant.ORDER_NO_PAYED_STATE);
		int kucun_num = damageListService.getDamageOrderListCount(shop.getId());

		/* 优惠券领取状况 */
		Date today = new Date();
		Date dayBeforeSeven = DateUtil.getDateBeforeWeek(today);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dateList = DateUtil.getTwoDay(sdf.format(today),
				sdf.format(dayBeforeSeven), false);
		List<CouponCode> codeList = couponCodeService.queryCouponCodeList(
				shop.getId(), dayBeforeSeven);
		int[] amountArr = new int[7];
		String[] dateArr = new String[7];
		for (int i = 0; i < dateList.size(); i++) {
			dateArr[dateList.size() - 1 - i] = dateList.get(i);
			if (codeList.size() > i) {
				amountArr[i] = codeList.get(i).getAmount();
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

		resultMap.put("jin_num1", jin_num1);
		resultMap.put("jin_num2", jin_num2);
		resultMap.put("sale_num1", sale_num1);
		resultMap.put("sale_num2", sale_num2);
		resultMap.put("kucun_num", kucun_num);
		return resultMap;
	}
	@PostMapping("/user")
	public String getUser(HttpServletRequest request)
			throws ParseException {
		User user = getCurrentUser(request);
		return user.getUserAccount();
	}
}
