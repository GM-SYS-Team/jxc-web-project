package com.gms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.PurchaseList;
import com.gms.entity.jxc.PurchaseListGoods;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.Supplier;
import com.gms.entity.jxc.User;
import com.gms.exception.MyException;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.PurchaseListService;
import com.gms.service.jxc.ShopService;
import com.gms.service.jxc.SupplierService;
import com.gms.util.Constant;
import com.gms.util.DateUtil;
import com.gms.util.StringUtil;
import com.google.common.base.Preconditions;

@Controller
@RequestMapping("/app/jxc/purchase")
@NeedAuth
public class JxcPurchaseController extends BaseAppController {
	
	@Autowired
	private PurchaseListService purchaseListService;
	
	@Autowired
	private SupplierService supplierService;

	@Autowired
	private ShopService shopService;
	
	@Autowired
	private GoodsService goodsService;
	
	/**
	 * 添加进货单
	 * @param goodsId 商品ID int
	 * @param supplierId 供应商ID int
	 * @param shopId 店铺ID int
	 * @param price 单价  float
	 * @param num 数量  int
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/save")
	public Map<String,Object> save(Integer goodsId, Integer supplierId, Integer shopId, Float price, Integer num)throws Exception{
		User user = getUser();
		validateUser(user, User.SHOPER);
		Preconditions.checkNotNull(goodsId, "商品ID不能为空");
		Preconditions.checkNotNull(shopId, "店铺ID不能为空");
		Preconditions.checkNotNull(supplierId, "请选择供应商");
		Preconditions.checkNotNull(price, "商品价格不能为空");
		Preconditions.checkNotNull(num, "商品数量不能为空");
		Preconditions.checkArgument(goodsId > 0, "商品ID错误"); 
		Preconditions.checkArgument(shopId > 0, "店铺ID错误"); 
		Preconditions.checkArgument(supplierId > 0, "供应商ID错误"); 
		Preconditions.checkArgument(price > 0f, "商品价格错误"); 
		Preconditions.checkArgument(num > 0, "商品数量错误"); 
		Shop shop = shopService.queryShopByShopIdAndUserId(shopId, user.getId());
		if (shop == null) {
			throw new MyException("店铺不存在");
		}
		Supplier supplier = supplierService.findById(supplierId);
		if (supplier == null || shopId != supplier.getShopId()) {
			throw new MyException("供应商不存在");
		}
		Goods goods = null;
		goods = goodsService.findById(goodsId);
		if (goods == null || shopId != goods.getShopId()) {
			throw new MyException("商品不存在");
		}
		if (goods.getInventoryQuantity() - num < goods.getMinNum()) {
			throw new MyException("购买数量超出商品库存");
		}
		//生成进货单号
		String purchaseNumber = genPurchaseNumber();
		//构造进货单
		PurchaseList purchaseList = new PurchaseList();
		purchaseList.setUser(user);
		purchaseList.setShopId(shopId);
		purchaseList.setPurchaseNumber(purchaseNumber);
		purchaseList.setSupplier(supplier);
		purchaseList.setPurchaseDate(new Date());
		purchaseList.setState(Constant.ORDER_PAYED_STATE);
		//计算购买金额
		Float amount = price * num;
		purchaseList.setAmountPaid(amount);
		purchaseList.setAmountPayable(amount);
		//构造进货单商品
		PurchaseListGoods purchaseListGoods = new PurchaseListGoods();
		purchaseListGoods.setPurchaseList(purchaseList);
		purchaseListGoods.setGoodsId(goodsId);
		purchaseListGoods.setModel(goods.getModel());
		purchaseListGoods.setName(goods.getName());
		purchaseListGoods.setType(goods.getType());
		purchaseListGoods.setTypeId(goods.getType().getId());
		purchaseListGoods.setNum(num);
		purchaseListGoods.setPrice(price);
		purchaseListGoods.setUnit(goods.getUnit());
		purchaseListGoods.setTotal(amount);
		List<PurchaseListGoods> list = new ArrayList<PurchaseListGoods>();
		list.add(purchaseListGoods);
		purchaseListService.save(purchaseList, list);
		return success();
	}
	
	/**
	 * 生成进货单号
	 * @return
	 * @throws Exception
	 */
	private String genPurchaseNumber()throws Exception{
		StringBuffer biilCodeStr=new StringBuffer();
		biilCodeStr.append("JH");
		biilCodeStr.append(DateUtil.getCurrentDateStr()); // 拼接当前日期
		String purchaseNumber=purchaseListService.getTodayMaxPurchaseNumber(); // 获取当天最大的进货单号
		if(purchaseNumber!=null){
			biilCodeStr.append(StringUtil.formatCode(purchaseNumber));
		}else{
			biilCodeStr.append(Constant.DEFAULT_TABLE_CODE);
		}
		return biilCodeStr.toString();
	}
}
