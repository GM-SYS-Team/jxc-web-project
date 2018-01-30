package com.gms.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.GoodsType;

/**
 * 商品类别Repository接口
 * @author jxc 
 *
 */
public interface GoodsTypeRepository extends JpaRepository<GoodsType, Integer>,JpaSpecificationExecutor<GoodsType>{

	/**
	 * 根据父节点查找商品类别
	 * @param parentId
	 * @return
	 */
	@Query(value="select * from t_goodstype where p_id=?1",nativeQuery=true)
	public List<GoodsType> findByParentId(int parentId);
	@Query(value="select * from t_goodstype where p_id=?1 and shop_id=?2",nativeQuery=true)
	public List<GoodsType> findByParentIdAndShopId(int parentId,Integer shopId);
	@Query(value="select * from t_goodstype where shop_id=?1 order by p_id",nativeQuery=true)
	public List<GoodsType> findAllByShopId(Integer shopId);
}
