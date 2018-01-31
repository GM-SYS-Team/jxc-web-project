package com.gms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.Customer;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.SaleList;
import com.gms.entity.jxc.SaleListGoods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.CustomerService;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.SaleListService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/app/jxc/saleList")
@NeedAuth
public class JxcSaleListController extends BaseAppController {

	@Autowired
	private SaleListService saleListService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private GoodsService goodsService;
	/**
	 * 查询订单列表(page和rows不输入则为查询所有)
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
		User user = getUser();
		validateUser(user, User.SHOPER);
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		SaleList saleList = new SaleList();
		saleList.setShopId(shopId);
		Long total = saleListService.getCount(saleList);
		if (page == null) {
			page = 1;
			rows = total.intValue();
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<SaleList> saleListList = saleListService.list(saleList, page, rows, Direction.DESC, "saleDate");
//		for (SaleList tempSaleList : saleListList) {
//			List<SaleListGoods> saleListGoodsList = saleListGoodsService.listBySaleListId(tempSaleList.getId());
//			tempSaleList.setSaleListGoodsList(saleListGoodsList);
//		}
		resultMap.put("rows", saleListList);
		resultMap.put("total", total);
		return success(resultMap);
	}

	/**
	 * 查询单个销售单的详细信息
	 * 
	 * @param saleListId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/detail")
	@ResponseBody
	public Map<String, Object> detail(Integer saleListId) throws Exception {
		if (saleListId == null || saleListId <= 0) {
			throw new MyException("非法请求");
		}
		User user = getUser();
		validateUser(user, User.SHOPER);
		SaleList saleList = saleListService.findById(saleListId);
		Integer shopId = saleList.getShopId();
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
//		List<SaleListGoods> saleListGoodsList = saleListGoodsService.listBySaleListId(saleList.getId());
//		saleList.setSaleListGoodsList(saleListGoodsList);
		return success(saleList);
	}
	
	
	/**
	 * 商品销售出库
	 * 
	 * @param saleListId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/sale")
	@ResponseBody
	public Map<String, Object> sale(Integer shopId, String data, Integer customerId) throws Exception {
		User user = getUser();
		validateUser(user, User.SHOPER);
		Preconditions.checkArgument(customerId > 0, "请选择购买用户"); 
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Preconditions.checkArgument(StringUtils.isNotBlank(data), "参数错误");
		List<Goods> goodsList = JSONObject.parseArray(data, Goods.class);
		if (goodsList == null || goodsList.size() == 0) {
			throw new MyException("参数错误");
		}
		Customer customer = customerService.findById(customerId);
		if (customer.getShopId() != shopId) {
			throw new MyException("客户不存在");
		}
		List<SaleListGoods> saleListGoodslist = new ArrayList<SaleListGoods>();
		SaleList saleList = new SaleList();
		Float amount = 0f;
		for (Goods goods : goodsList) {
			Preconditions.checkArgument(StringUtils.isNotBlank(goods.getName()), "参数错误，商品名称不能为空");
			Preconditions.checkArgument(goods.getMinNum() > 0, goods.getName() + "的数量不能为空"); 
			Preconditions.checkArgument(goods.getSellingPrice() > 0, goods.getName() + "的售价不能为空"); 
			Goods tempGoods = goodsService.findById(goods.getId());
			if (tempGoods == null || tempGoods.getShopId() != shopId) {
				throw new MyException("商品[" + goods.getName() + "]不存在");
			}
			amount = amount + goods.getMinNum() * goods.getSellingPrice();
			convertGoodsToSaleListGoods(goods, tempGoods, saleListGoodslist, saleList);
		}
		saleList.setSaleNumber(genSaleNumber());
		saleList.setAmountPaid(amount);
		saleList.setAmountPayable(amount);
		saleList.setCustomer(customer);
		saleList.setUser(user);
		saleList.setState(Constant.ORDER_PAYED_STATE);
		saleList.setShopId(shopId);
		saleList.setSaleDate(new Date());
		saleListService.save(saleList, saleListGoodslist);
		return success();
	}
	
	
	
	private void convertGoodsToSaleListGoods(Goods goods, Goods tempGoods, List<SaleListGoods> saleListGoodslist, SaleList saleList) {
		SaleListGoods saleListGoods = new SaleListGoods();
		saleListGoods.setName(tempGoods.getName());
		saleListGoods.setModel(tempGoods.getModel());
		saleListGoods.setType(tempGoods.getType());
		saleListGoods.setTypeId(tempGoods.getType().getId());
		saleListGoods.setGoodsId(tempGoods.getId());
		saleListGoods.setUnit(tempGoods.getUnit());
		saleListGoods.setPrice(goods.getSellingPrice());
		saleListGoods.setNum(goods.getMinNum());
		saleListGoods.setTotal(goods.getSellingPrice() * goods.getMinNum());
		saleListGoodslist.add(saleListGoods);
	}

	/**
	 * 获取销售单号
	 * @return
	 * @throws Exception
	 */
	public String genSaleNumber()throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("XS");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String saleNumber=saleListService.getTodayMaxSaleNumber(); // 获取当天最大的销售单号
		if(saleNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(saleNumber));
		}else{
			biilCodeStr.append(Constant.DEFAULT_TABLE_CODE);
		}
		return biilCodeStr.toString();
	}
}
