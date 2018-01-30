package com.gms.service.jxc;

import java.util.List;

import com.gms.entity.jxc.GoodsType;

/**
 * 商品类别Service接口
 * @author jxc 
 *
 */
public interface GoodsTypeService {

	/**
	 * 根据id查询商品类别实体
	 * @param id
	 * @return
	 */
	public GoodsType findById(Integer id);
	
	/**
	 * 根据父节点查找商品类别
	 * @param parentId
	 * @return
	 */
	public List<GoodsType> getAllByParentId(int parentId);
	public List<GoodsType> getAllByParentIdAndShopId(int parentId,Integer shopId);
	
	/**
	 * 添加或者修改商品类别信息
	 * @param goodsType
	 */
	public void save(GoodsType goodsType);
	
	/**
	 * 根据id删除商品类别信息
	 * @param id
	 */
	public void delete(Integer id);

	/**
	 * 根据店铺id查询商品品类
	 * @param shopId
	 * @return
	 */
	public List<GoodsType> getAllByShopId(Integer shopId);
}
