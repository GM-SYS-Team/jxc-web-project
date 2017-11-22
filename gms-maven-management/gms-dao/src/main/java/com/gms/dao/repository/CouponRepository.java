package com.gms.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.Coupon;


/** 
* @ClassName: CouponsManageDao 
* @Description: TODO
* @author shenlinli
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponRepository extends JpaRepository<Coupon, Integer>,JpaSpecificationExecutor<Coupon>{

	@Query(value="select * from t_coupon where status=?1",nativeQuery=true)
	public List<Coupon> findCouponByStatus(Integer status);
	
}