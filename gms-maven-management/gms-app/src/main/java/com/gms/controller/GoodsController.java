package com.gms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.ShopService;
import com.gms.util.StringUtil;

@Controller
@RequestMapping("/app/goods")
@NeedAuth
public class GoodsController extends BaseAppController {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private CouponService couponService;
	
	//文件存储路径
	@Value(value = "${picuploadPath}")
	private String picturePath;
	
	
	/**
	 * 根据条件分页查询商品信息
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows, Integer shopId)throws Exception{
		if (shopId == 0 || shopId <= 0) {
			return error("非法请求");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Goods goods = new Goods();
		goods.setShopId(shopId);
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		Long total=goodsService.getCount(goods);
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		return success(resultMap);
	}
	
	
	/**
	 * 添加商品
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(Goods goods)throws Exception{
		User user = getUser();
		Integer shopId = goods.getShopId();
		String pictureAddress = goods.getPictureAddress();
		String goodsName = goods.getName();
		if (StringUtil.isEmpty(goodsName)) {
			return error("商品名称不能为空");
		}
		if (shopId <= 0) {
			return error("店铺ID不能为空");
		}
		if (StringUtil.isEmpty(pictureAddress)) {
			return error("图片地址不能为空");
		}
		if (!pictureAddress.startsWith("picturePath")) {
			return error("图片地址错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		goodsService.save(goods);
		return success(goods);
	}
	
	/**
	 * 添加商品
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/modify")
	@ResponseBody
	public Map<String,Object> modify(Goods goods)throws Exception{
		User user = getUser();
		Integer shopId = goods.getShopId();
		String pictureAddress = goods.getPictureAddress();
		String goodsName = goods.getName();
		Integer goodsId = goods.getId();
		if (goodsId == null || goodsId <= 0) {
			return error("商品Id不能为空");
		}
		if (StringUtil.isEmpty(goodsName)) {
			return error("商品名称不能为空");
		}
		if (shopId <= 0) {
			return error("店铺ID不能为空");
		}
		if (StringUtil.isEmpty(pictureAddress)) {
			return error("图片地址不能为空");
		}
		if (!pictureAddress.startsWith("picturePath")) {
			return error("图片地址错误");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Goods tmpGoods=goodsService.findById(goodsId);
		if (tmpGoods == null) {
			return error("商品不存在");
		}
		if (shopId != tmpGoods.getShopId()) {
			return error("商品不存在");
		}
		goodsService.save(goods);
		return success();
	}
	
	/**
	 * 删除商品信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(Integer goodsId, Integer shopId)throws Exception{
		if (goodsId == null || goodsId <= 0) {
			return error("商品Id不能为空");
		}
		if (shopId == null || shopId <= 0) {
			return error("店铺Id不能为空");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		Goods goods=goodsService.findById(goodsId);
		if (goods == null) {
			return error("商品不存在");
		}
		if (shopId != goods.getShopId()) {
			return error("商品不存在");
		}
		List<CouponGoods> couponGoodsList = couponService.findCouponListByGoodsId(goodsId);
		if (couponGoodsList != null && couponGoodsList.size() > 0) {
			return error("该商品已经和优惠券关联，无法删除");
		}
		goodsService.delete(goodsId);							
		return success();
	}
	
}
