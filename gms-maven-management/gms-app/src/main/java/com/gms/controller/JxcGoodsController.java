package com.gms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.annoation.NeedAuth;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.CouponGoods;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.GoodsType;
import com.gms.entity.jxc.GoodsUnit;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.CouponService;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.GoodsTypeService;
import com.gms.service.jxc.GoodsUnitService;
import com.gms.service.jxc.ShopService;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/app/jxc/goods")
@NeedAuth
public class JxcGoodsController extends BaseAppController {

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private CouponService couponService;
	
	@Autowired
	private GoodsUnitService goodsUnitService;
	
	@Autowired
	private GoodsTypeService goodsTypeService;
	               
	@Autowired
	private ImageServerProperties imageServerProperties;

	/**
	 * 根据条件分页查询商品信息(page和rows不输入则为查询所有商品)
	 * 
	 * @param shopId
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows, Integer shopId) throws Exception {
		if (shopId == 0 || shopId <= 0) {
			throw new MyException("非法请求");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Goods goods = new Goods();
		goods.setShopId(shopId);
		Long total = goodsService.getCount(goods);
		if (page == null) {
			page = 1;
			rows = total.intValue();
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList = goodsService.list(goods, page, rows, Direction.ASC, "id");
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		return success(resultMap);
	}

	
	
	
	/**
	 * 添加商品
	 * 
	 * @param name 商品名称  String
	 * @param model 商品型号 String
	 * @param unit 商品单位 String 取listUnit接口下的name字段的值
	 * @param purchasingPrice 商品采购价 Float
	 * @param sellingPrice 商品销售价 Float
	 * @param minNum 库存下限 Integer
	 * @param inventoryQuantity 库存数量 Integer
	 * @param goodsType 商品品类 Integer 取/ListType接口下的id字段的值
	 * @param pictureAddress 图片地址 String
	 * @param shopId 店铺Id Integer
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@ResponseBody
	public Map<String, Object> save(Goods goods, Integer goodsTypeId) throws Exception {
		User user = getUser();
		Integer shopId = goods.getShopId();
		String pictureAddress = goods.getPictureAddress();
		Preconditions.checkArgument(StringUtils.isNotBlank(goods.getName()), "商品名称不能为空");
		Preconditions.checkArgument(goods.getName().length() < 50, "商品名称长度不能超过50");
		Preconditions.checkArgument(StringUtils.isNotBlank(goods.getModel()), "商品型号不能为空");
		Preconditions.checkArgument(goods.getModel().length() < 30, "型号长度不能超过30");
		Preconditions.checkArgument(StringUtils.isNotBlank(goods.getUnit()), "商品单位不能为空");
		Preconditions.checkArgument(goods.getUnit().length() < 10, "商品单位长度不能超过10");
		Preconditions.checkArgument(goods.getPurchasingPrice() > 0f, "商品采购价错误");
		Preconditions.checkArgument(goods.getSellingPrice() > 0f, "商品售价错误");
		Preconditions.checkArgument(goods.getMinNum() > 0, "库存下限不能小于0");
		Preconditions.checkArgument(goods.getInventoryQuantity() > 0, "库存数量不能小于0");
		Preconditions.checkArgument(goodsTypeId != null && goodsTypeId > 0, "商品品类不能为空");
		Preconditions.checkArgument(shopId >0, "店铺ID不能为空");
		Preconditions.checkArgument(StringUtils.isNotBlank(goods.getPictureAddress()), "图片地址不能为空");
		String url = imageServerProperties.getUrl();
		url = url.substring(0, url.lastIndexOf(":"));
		if (!pictureAddress.startsWith(url)) {
			throw new MyException("图片地址错误");
		}
		
		if (!checkGoodsTypeId(goodsTypeId, shopId, goods)) {
			throw new MyException("商品品类不存在");
		}
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		goodsService.save(goods);
		return success(goods);
	}

	private boolean checkGoodsTypeId(Integer goodsTypeId, Integer shopId, Goods goods) {
		List<GoodsType> list = goodsTypeService.getAllByShopId(shopId);
		for (GoodsType temp : list) {
			if (temp.getId() == goodsTypeId) {
				goods.setType(temp);
				return true;
			}
		}
		return false;
	}




	/**
	 * 删除商品信息
	 * 
	 * @param goodsId  Integer
	 * @param shopId  Integer
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer goodsId, Integer shopId) throws Exception {
		if (goodsId == null || goodsId <= 0) {
			throw new MyException("商品Id不能为空");
		}
		if (shopId == null || shopId <= 0) {
			throw new MyException("店铺Id不能为空");
		}
		User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Goods goods = goodsService.findById(goodsId);
		if (goods == null) {
			throw new MyException("商品不存在");
		}
		if (shopId != goods.getShopId()) {
			throw new MyException("商品不存在");
		}
		List<CouponGoods> couponGoodsList = couponService.findCouponListByGoodsId(goodsId);
		if (couponGoodsList != null && couponGoodsList.size() > 0) {
			throw new MyException("该商品已经和优惠券关联，无法删除");
		}
		if(goods.getState()==1){
			throw new MyException("该商品已经期初入库，不能删除！");
		}
		if(goods.getState()==2){
			throw new MyException("该商品已经发生单据，不能删除！");
		}
		goodsService.delete(goodsId);
		return success();
	}
	
	
	/**
	 * 查询商品规格
	 * 
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listUnit")
	@ResponseBody
	public Map<String, Object> listUnit() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		List<GoodsUnit> list = goodsUnitService.listAll();
		resultMap.put("rows", list);
		return success(resultMap);
	}
	
	/**
	 * 加载商品类别树菜单
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/listType")
    @ResponseBody
	public  Map<String, Object> listType(Integer shopId)throws Exception{
    	if (shopId == null || shopId <= 0) {
			throw new MyException("店铺Id不能为空");
		}
    	User user = getUser();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
    	List<GoodsType> list = goodsTypeService.getAllByShopId(shopId);
    	if(list==null || list.isEmpty()){
    		GoodsType type = new GoodsType();
    		type.setName("所有类别");
    		type.setpId(-1);
    		type.setIcon("icon-folderOpen");
    		type.setState(0);
    		type.setShopId(shopId);
    		goodsTypeService.save(type);
    		list = new ArrayList<GoodsType>();
    		list.add(type);
    	}
    	Map<String, Object> resultMap = new HashMap<>();
    	resultMap.put("rows", list);
		return success(resultMap);
	}
}
