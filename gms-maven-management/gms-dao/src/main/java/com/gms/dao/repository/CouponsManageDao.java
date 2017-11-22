package com.gms.dao.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.Customer;


/** 
* @ClassName: CouponsManageDao 
* @Description: TODO
* @author shenlinli
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponsManageDao extends JpaRepository<Customer, Integer>,JpaSpecificationExecutor<Customer>{

	@Query(value="select * from t_coupon ",nativeQuery=true)
	public List<Coupon> findCouponList();
}
