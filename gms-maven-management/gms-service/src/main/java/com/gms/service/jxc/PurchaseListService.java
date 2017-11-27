package com.gms.service.jxc;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.gms.entity.jxc.PurchaseList;
import com.gms.entity.jxc.PurchaseListGoods;

/**
 * 进货单Service接口
 * @author jxc_
 *
 */
public interface PurchaseListService {

	/**
	 * 根据id查询实体
	 * @param id
	 * @return
	 */
	public PurchaseList findById(Integer id);
	
	/**
	 * 获取当天最大进货单号
	 * @return
	 */
	public String getTodayMaxPurchaseNumber();
	
	/**
	 * 添加进货单 以及所有进货单商品 以及 修改商品的成本均价
	 * @param purchaseList 进货单
	 * @param PurchaseListGoodsList 进货单商品
	 */
	public void save(PurchaseList purchaseList,List<PurchaseListGoods> purchaseListGoodsList);
	
	/**
	 * 根据条件查询进货单信息
	 * @param purchaseList
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<PurchaseList> list(PurchaseList purchaseList,Direction direction,String... properties);
	
	/**
	 * 根据id删除进货单信息 包括进货单里的商品
	 * @param id
	 */
	public void delete(Integer id);
	
	/**
	 * 更新进货单
	 * @param purchaseList
	 */
	public void update(PurchaseList purchaseList);
	/**
	 * 获取已支付的进货单数
	 * @param shopId
	 */
	public int getPurchasePayedListCount(Integer shopId,Integer state);
}
