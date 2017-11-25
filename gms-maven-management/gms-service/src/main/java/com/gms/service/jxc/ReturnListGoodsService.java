package com.gms.service.jxc;

import java.util.List;

import com.gms.entity.jxc.ReturnListGoods;

/**
 * 退货单商品Service接口
 * @author jxc_
 *
 */
public interface ReturnListGoodsService {

	/**
	 * 根据退货单id查询所有退货单商品
	 * @param returnListId
	 * @return
	 */
	public List<ReturnListGoods> listByReturnListId(Integer returnListId);
	
	/**
	 * 根据条件查询退货单所有商品
	 * @param returnListGoods
	 * @return
	 */
	public List<ReturnListGoods> list(ReturnListGoods returnListGoods);
}
