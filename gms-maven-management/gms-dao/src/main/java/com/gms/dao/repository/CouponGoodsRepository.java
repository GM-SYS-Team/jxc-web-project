package com.gms.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.CouponGoods;


/** 
* @ClassName: CouponGoodsRepository 
* @Description: TODO
* @author shenlinli
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponGoodsRepository extends JpaRepository<CouponGoods, Integer>,JpaSpecificationExecutor<CouponGoods>{
	@Query(value="delete from t_coupon_goods where coupon_id = ?1 and shop_id = ?2",nativeQuery=true)
	@Modifying
	public void deleteCouponGoods(Integer couponId,Integer shopId);

	@Query(value="select * from t_coupon_goods where good_id = ?1",nativeQuery=true)
	public List<CouponGoods> findCouponListByGoodsId(Integer goodsId); 
	
	
}
