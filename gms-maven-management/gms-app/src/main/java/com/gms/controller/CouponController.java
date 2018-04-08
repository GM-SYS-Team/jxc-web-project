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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gms.annoation.NeedAuth;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.CouponCodeService;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.HttpsUtil;
import com.gms.util.StringUtil;

@Controller
@RequestMapping("/app/coupon")
public class CouponController extends BaseAppController {
	@Autowired
	private CouponService couponService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private CouponCodeService couponCodeService;

	@Autowired
	private ImageServerProperties imageServerProperties;
	
	@Autowired
	private GoodsService goodsService;
	
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
	@NeedAuth
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
		if (minAmount > maxAmount) {
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
		
		Date now = new Date();
		if (now.after(coupon.getExpiryDateStart())) {
			throw new MyException("优惠券开始时间不能小于当前时间");
		}
		if (now.after(coupon.getExpiryDateStop())) {
			throw new MyException("优惠券结束时间不能小于当前时间");
		}
		if (coupon.getExpiryDateStop().before(coupon.getExpiryDateStart())) {
			throw new MyException("优惠券开始时间不能大于结束时间");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		coupon.setShopId(shopId);
		Goods goods = goodsService.findById(goodsId);
		if (goods == null) {
			return error("商品不存在");
		}
		if (goods.getShopId() != shopId) {
			return error("商品不存在");
		}
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
				return success(coupon);
			} else {
				throw new MyException("服务器请假了，请稍后重试");
			}
		}
		return error("服务器请假了，请稍后重试");
	}

	/**
	 * 修改优惠券信息
	 * 
	 * @param request
	 * @return
	 * @throws ParseException
	 * @throws MyException
	 */
	@RequestMapping("/modify")
	@ResponseBody
	@NeedAuth
	public Map<String, Object> modify(HttpServletRequest request) throws ParseException, MyException {
		User user = getUser();
		validateUser(user, User.SHOPER);
		Integer goodsId = Integer.parseInt(request.getParameter("goodsId"));
		/* 当前登录的店铺 */
		Integer couponId = Integer.parseInt(request.getParameter("couponId"));
		Integer shopId = Integer.parseInt(request.getParameter("shopId"));
		String couponName = request.getParameter("couponName");
		Integer totalCount = Integer.parseInt(request.getParameter("totalCount"));
		Double maxAmount = Double.parseDouble(request.getParameter("maxAmount"));
		Double minAmount = Double.parseDouble(request.getParameter("minAmount"));
		String expiryDateStart = request.getParameter("expiryDateStart");
		String expiryDateStop = request.getParameter("expiryDateStop");
		if (shopId == null || shopId <= 0) {
			throw new MyException("非法请求");
		}
		if (StringUtil.isEmpty(couponName)) {
			throw new MyException("请设置优惠券名称");
		}	
		if (couponName.length() > 50) {
			throw new MyException("优惠券名称长度不能超过50");
		}
		if (totalCount <= 0) {
			throw new MyException("请设置优惠券数量");
		}
//		if (StringUtil.isEmpty(couponInfo)) {
//			return error("请设置优惠券描述");
//		}
//		if (couponInfo.length() > 500) {
//			return error("优惠券描述的长度不能超过500");
//		}
		if (maxAmount <= 0 || minAmount <= 0) {
			throw new MyException("请设置优惠券金额");
		}
		if (goodsId == null || goodsId <= 0) {
			throw new MyException("请选择商品");
		}
		if (minAmount > maxAmount) {
			throw new MyException("优惠券最大金额不得低于优惠券最小金额");
		}
		if (StringUtil.isEmpty(expiryDateStart)) {
			throw new MyException("优惠券开始时间不能为空");
		}
		if (StringUtil.isEmpty(expiryDateStop)) {
			throw new MyException("优惠券结束时间不能为空");
		}
		Date startTime = null;
		Date endTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			startTime = sdf.parse(expiryDateStart);
			endTime = sdf.parse(expiryDateStop);
		} catch (Exception e) {
			throw new MyException("优惠券开始时间和结束时间参数错误");
		}
		Date now = new Date();
		if (endTime.before(now)) {
			throw new MyException("结束时间不可小于当前时间");
		}
		if (startTime.after(endTime)) {
			throw new MyException("开始时间不可晚于结束时间");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Coupon coupon = couponService.findCouponById(couponId);
		if (coupon == null) {
			throw new MyException("优惠券不存在");
		}
		if (coupon.getShopId() != shopId) {
			throw new MyException("优惠券不存在");
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
	@NeedAuth
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
	@NeedAuth
	public Map<String, Object> list(Integer state, Integer shopId,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		if (shopId == null || shopId <= 0) {
			return error("店铺id错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		if (page == null) {
			page = 1;
			rows = 10;
		}
		Coupon ParamCoupon = new Coupon();
		ParamCoupon.setShopId(shopId);
		List<Coupon> couponList = new ArrayList<Coupon>();
		/* 当前登录的店铺 */
		couponList = couponService.list(ParamCoupon, page, rows, Direction.ASC, state, "id");
		Long couponListSize = couponService.listCount(ParamCoupon, state);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("couponList", couponList);
		resultMap.put("size", couponListSize);
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
	@NeedAuth
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
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/list")
	@ResponseBody
	@NeedAuth
	public Map<String, Object> listUserCoupon(Integer state,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows) throws Exception {
		User user = getUser();
		validateUser(user, User.CUSTOMER);
		Page<CouponCode> pageCouponCode = couponCodeService.list(user.getId(), state, page, rows, Direction.ASC, "id");
		List<CouponCode> list = pageCouponCode.getContent();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("couponList", list);
		resultMap.put("size", pageCouponCode.getTotalElements());
		return success(resultMap);
	}

	/**
	 * 领券
	 * 
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/receive")
	@ResponseBody
	@NeedAuth
	@Transactional
	public Map<String, Object> receiveCoupon(Integer couponId, Integer shopId) throws Exception {
		User user = getUser();
		if (!User.CUSTOMER.equals(user.getUserType())) {
			throw new MyException("请使用用户帐号登录领取优惠券	");
		}
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
			CouponGoods couponGoods = coupon.getCouponGoods();
			Goods goods = goodsService.findById(couponGoods.getGoodId());
			couponCode.setReceiveTime(now);
			couponCode.setCoupon(coupon);
			couponCode.setAmount(
					(int) (Math.random() * (coupon.getMaxAmount() - coupon.getMinAmount()) + coupon.getMinAmount()));
			couponCode.setUserId(user.getId());
			couponCode.setIsUsed(Constant.COUPON_NOT_USED);
			couponCode.setExpiryDateStart(coupon.getExpiryDateStart());
			couponCode.setExpiryDateStop(coupon.getExpiryDateStop());
			couponCode.setGoodsPicUrl(goods.getPictureAddress());
			coupon.setRemainCount(coupon.getRemainCount() - 1);
			couponService.save(coupon);
			couponCodeService.save(couponCode);
			// FIXME 此处添加二维码生成的接口代码
			String result = HttpsUtil.getInstance().sendHttpPost(
					imageServerProperties.getUrl() + "/" + imageServerProperties.getQuickMarkAction(),
					"quickMarkStr=000000" + couponCode.getId()+ "&markType=" + Constant.QUICK_MARK_CUSTOMER_TYPE
							+ "&quickMarkRows=" + "" + "&quickMarkCols=" + "" + "&quickMarkModelSize=" + ""
							+ "&quickMarkQzsize=" + "" + "&quickMarkType=" + "");
			if (result != null) {
				String quickMarkImageName = null;
				JSONObject resultJson = (JSONObject) JSONObject.parse(result);
				if (resultJson.getString("message").equals("Ok")) {
					quickMarkImageName = resultJson.getJSONObject("data").getString("url");
					Map<String, String> maps = new HashMap<String, String>();
					maps.put("quickAddress", quickMarkImageName);
					maps.put("goodsAddress", goods.getPictureAddress());
					result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl() + "/" + imageServerProperties.getRealQuickMarkAction() ,maps);
					if (result != null) {
						resultJson = (JSONObject) JSONObject.parse(result);
						if (resultJson.getString("message").equals("Ok")) {
							quickMarkImageName = resultJson.getJSONObject("data").getString("url");
							couponCode.setQuickMark(quickMarkImageName);
							couponCodeService.save(couponCode);
							return success(couponCode);
						}
					}
				} else {
					throw new MyException("服务器请假了，请稍后重试");
				}
			}
		}
		throw new MyException("服务器请假了，请稍后重试");
	}

	/**
	 * 销券
	 * 
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/sale")
	@ResponseBody
	@NeedAuth
	public Map<String, Object> saleCoupon(String id) throws Exception {
		//去除前六位六个0
		id = id.substring(6);
		Integer couponCodeId = Integer.valueOf(id);
		User user = getUser();
		validateUser(user, User.SHOPER);
		CouponCode couponCode = couponCodeService.findCouponCode(couponCodeId);
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
		if (Constant.COUPON_USED.equals(couponCode.getIsUsed())) {
			return error("优惠券已使用");
		}
		couponCode.setUsedTime(now);
		couponCode.setIsUsed(Constant.COUPON_USED);
		couponCodeService.save(couponCode);
		return success();
	}
	
	/**
	 * 查询优惠券状态
	 * 
	 * @param status
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/couponCodeState")
	@ResponseBody
	@NeedAuth
	public Map<String, Object> couponCodeState(Integer couponCodeId, Integer ownerId) throws Exception {
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
		if (Constant.COUPON_USED.equals(couponCode.getIsUsed())) {
			return error("优惠券已使用");
		}
		return success(couponCode);
	}

	/**
	 * 查询用户可以领取的优惠券
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user")
	@ResponseBody
	public Map<String, Object> getCoupon(Integer shopId) throws Exception {
//		User user = getUser();
//		validateUser(user, User.CUSTOMER);
		Date now = new Date();
		List<Coupon> couponList = new ArrayList<Coupon>();
		Coupon ParamCoupon = new Coupon();
		ParamCoupon.setShopId(shopId);
		/* 当前登录的店铺 */
		couponList = couponService.list(ParamCoupon, 1, 10, Direction.ASC, 2, "id");
		Coupon randomCoupon = couponService.findRandomCoupon(now, shopId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("couponList", couponList);
		map.put("shareCoupon", randomCoupon);
		return success(map);
	}
	
	

	/**
	 * 查询用户可以领取的优惠券
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/tuijian")
	@ResponseBody
	@NeedAuth
	public Map<String, Object> tuijian() throws Exception {
		User user = getUser();
		validateUser(user, User.CUSTOMER);
		Date now = new Date();
		/* 当前登录的店铺 */
		List<Coupon> randomCouponList = couponService.findRandomCouponList(now);
		for (Coupon coupon : randomCouponList) {
			Goods goods = goodsService.findById(coupon.getCouponGoods().getGoodId());
			coupon.setGoodsPic(goods.getPictureAddress());
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("couponList", randomCouponList);
		return success(map);
	}
	
	public static void main(String[] args) {
		String id = "000000123";
		id = id.substring(6);
		Integer couponCodeId = Integer.valueOf(id);
		System.out.println(couponCodeId);
	}
}
