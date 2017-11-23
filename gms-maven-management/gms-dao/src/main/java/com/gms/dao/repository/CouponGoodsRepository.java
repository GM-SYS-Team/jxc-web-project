package com.gms.dao.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gms.entity.jxc.CouponGoods;


/** 
* @ClassName: CouponGoodsRepository 
* @Description: TODO
* @author shenlinli
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponGoodsRepository extends JpaRepository<CouponGoods, Integer>,JpaSpecificationExecutor<CouponGoods>{

	
}
