package com.gms.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.Goods;


/**
 * 商品Repository接口
 * @author jxc 
 *
 */
public interface GoodsRepository extends JpaRepository<Goods, Integer>,JpaSpecificationExecutor<Goods>{

	/**
	 * 获取最大的商品编号
	 * @return
	 */
	@Query(value="select max(code) from t_goods",nativeQuery=true)
	public String getMaxGoodsCode();
	
	/**
	 * 查询库存报警商品 库存小于库存下限的商品
	 * @return
	 */
	@Query(value="SELECT * FROM t_goods WHERE inventory_quantity<min_num and shop_id=?1",nativeQuery=true)
	public List<Goods> listAlarm(Integer shopId);

	/**
	 * 根据店铺ID查询对应的商品
	 * @param shopId
	 * @return
	 */
	@Query(value="SELECT * FROM t_goods where shop_id=?1",nativeQuery=true)
	public List<Goods> findGoodsByShopId(Integer shopId);
	
}
