package com.gms.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Shop;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.LogService;
import com.gms.util.HttpsUtil;

@RestController
@RequestMapping("/admin/coupon")
public class CouponController extends BaseController {
	@Resource
	private LogService logService;
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private ImageServerProperties imageServerProperties;
	
	@Autowired
	private GoodsService goodsService;

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
		Integer goodsId = Integer.parseInt(request.getParameter("goodsId"));
		Goods goods = goodsService.findById(goodsId);
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		Map<String, Object> resultMap = new HashMap<>();
		Coupon coupon = new Coupon();
		coupon.setCouponName(request.getParameter("couponName"));
		coupon.setTotalCount(Integer.parseInt(request
				.getParameter("totalCount")));
		coupon.setRemainCount(Integer.parseInt(request
				.getParameter("totalCount")));
		coupon.setCouponInfo(request.getParameter("couponIntro"));
		/*coupon.setCouponCount(request.getParameter("couponCount"));*/
		coupon.setMinAmount(Double.parseDouble(request
				.getParameter("minAmount")));
		coupon.setMaxAmount(Double.parseDouble(request
				.getParameter("maxAmount")));
		coupon.setExpiryDateStart(sdf.parse(request
				.getParameter("startExpiryDate")));
		coupon.setExpiryDateStop(sdf.parse(request
				.getParameter("stopExpiryDate")));
		coupon.setShopId(shop.getId());
		
		//FIXME 这里添加生成二维码的代码
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("quickAddress", shop.getQuickMark());
		maps.put("goodsAddress", goods.getPictureAddress());
		String result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl() + "/" + imageServerProperties.getRealQuickMarkAction() ,maps);
		if (result != null) {
			String quickMark = null;
			JSONObject resultJson = (JSONObject) JSONObject.parse(result);
			if (resultJson.getString("message").equals("Ok")) {
				quickMark = resultJson.getJSONObject("data").getString("url");
				coupon.setQuickMark(quickMark);
				couponService.save(coupon);
				CouponGoods couponGoods = new CouponGoods();
				couponGoods.setCouponId(coupon.getId());
				couponGoods.setShopId(shop.getId());
				couponGoods.setGoodId(goodsId);
				couponService.saveCouponGoods(couponGoods);
				resultMap.put("success", true);
			} else {
				resultMap.put("success", false);
			}
		}else{
			resultMap.put("success", false);
		}
		/*couponService.save(coupon);
		for (String goodsId : goodsIds) {
			CouponGoods couponGoods = new CouponGoods();
			couponGoods.setCouponId(coupon.getId());
			couponGoods.setShopId(shop.getId());
			couponGoods.setGoodId(Integer.parseInt(goodsId));
			couponService.saveCouponGoods(couponGoods);
		}
		resultMap.put("success", true);*/
		return resultMap;
	}

	@PostMapping("/delete")
	public Map<String, Object> delete(
			@RequestParam(value = "id", required = true) Integer id,
			HttpServletRequest request) throws Exception {
		Coupon coupon = couponService.findCouponById(id);
		coupon.setStatus(Coupon.STATUS_DEL);
		couponService.save(coupon);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		logService.save(new Log(Log.SEARCH_ACTION, "删除优惠券-->id:" + id)); // 写入日志
		return resultMap;
	}

	@PostMapping("/list")
	public Map<String, Object> list(
			@RequestParam(value = "num", required = true) Integer num,
			HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows)
			throws Exception {
		/* 当前登录的店铺 */
		Shop shop = getCurrentShop(request);
		Coupon ParamCoupon = new Coupon();
		ParamCoupon.setShopId(shop.getId());
		List<Coupon> couponListResult = couponService.list(ParamCoupon, page,
				rows, Direction.ASC, num, "id");
		Long couponListSize = couponService.listCount(ParamCoupon, num);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("couponList", couponListResult);
		resultMap.put("size", couponListSize);
		logService.save(new Log(Log.SEARCH_ACTION, "查询优惠券信息")); // 写入日志
		return resultMap;
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
	public Map<String, Object> shareCoupon(Integer id,HttpServletRequest request) throws Exception {
		Coupon coupon = couponService.findCouponById(id);
		coupon.setStatus(Coupon.STATUS_SHARE);
		couponService.save(coupon);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		return resultMap;
	}
}