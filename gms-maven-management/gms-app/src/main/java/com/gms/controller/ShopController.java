package com.gms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gms.annoation.NeedAuth;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import com.gms.util.HttpsUtil;
import com.gms.util.StringUtil;

@Controller
@RequestMapping("/app/shop")
@NeedAuth
public class ShopController extends BaseAppController {
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ImageServerProperties imageServerProperties;
	
	/**
	 * 分页查询商铺信息
	 * @param customer
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		User user = getUser();
		Shop shop = new Shop();
		shop.setUserId(user.getId());
		if (!User.SHOPER.equals(user.getUserType())) {
			return error("该用户不是商户");
		}
		if (page == null) {
			page = 1;
			rows = 10;
		}
		List<Shop> shopList=shopService.list(shop, page, rows, Direction.ASC, "id");
		Long total=shopService.getCount(shop);
		Map<String, Object> resultMap = success();
		resultMap.put("rows", shopList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	
	
	/**
	 * 添加或者修改供应商信息
	 * @param supplier
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String,Object> save(Shop shop)throws Exception{
		User user = getUser();
		if (StringUtil.isEmpty(shop.getShopName())) {
			return error("店铺名称不能为空");
		}
		if (shop.getShopName().length() > 60) {
			return error("店铺名称太长");
		}
		if (!User.SHOPER.equals(user.getUserType())) {
			return error("该用户不是商户");
		}
		shop.setUpdateTime(new Date());
		if (shop.getId()==null){ 
			shop.setCreateTime(new Date());
		}
		shop.setUserId(user.getId());
		shopService.save(shop);
		String result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl()+"/"+
				imageServerProperties.getQuickMarkAction(),"quickMarkStr="+ shop.getId()
						+"&markType="+Constant.QUICK_MARK_SHOP_TYPE+"&quickMarkRows="+""+"&quickMarkCols=" +""
						+"&quickMarkModelSize=" +""+ "&quickMarkQzsize=" +""+ "&quickMarkType="+"");
		if(result!=null){
			String quickMarkImageName = null;
			JSONObject resultJson = (JSONObject)JSONObject.parse(result);
			if(resultJson.getString("message").equals("Ok")){
				quickMarkImageName = resultJson.getJSONObject("data").getString("url");
				shop.setQuickMark(quickMarkImageName);
				shopService.save(shop);
			}else{
				return error("服务器请假了，请稍后重试");
			}
		}
		return success(shop);
	}
	
	
	/**
	 * 删除供应商信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(Integer shopId)throws Exception{
		if (StringUtils.isEmpty(shopId) || shopId <= 0) {
			return error("shopId不能为空");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		List<Goods> goodsList = goodsService.findGoodsByShopId(shopId);
		if (goodsList != null && goodsList.size() > 0) {
			return error("该店铺下存在商品，不可删除");
		}	
		shopService.delete(shopId);							
		return success();
	}
	
	/**
	 * 根据条件分页查询商品信息
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/query")
	@ResponseBody
	public Map<String,Object> query(Integer shopId)throws Exception{
		if (shopId == 0 || shopId <= 0) {
			return error("非法请求");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			return error("店铺不存在");
		}
		return success(shop);
	}
}
