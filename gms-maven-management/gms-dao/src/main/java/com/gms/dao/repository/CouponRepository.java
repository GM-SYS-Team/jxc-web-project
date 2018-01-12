package com.gms.dao.repository;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.Coupon;


/** 
* @ClassName: CouponsManageDao 
* @Description: TODO
* @author shenlinli
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponRepository extends JpaRepository<Coupon, Integer>,JpaSpecificationExecutor<Coupon>{
	@Transactional
	@Modifying
	@Query(value="delete from t_coupon where id = ?1",nativeQuery=true)
	public void deleteCoupon(Integer couponId);
	
	@Query(value="select * from t_coupon where expiry_date_start>?1 and shop_id = ?2 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponBygt(Date date,Integer shopId);
	
	@Query(value="select * from t_coupon where expiry_date_stop<?1 and shop_id = ?2 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponBylt(Date date,Integer shopId);
	
	@Query(value="select * from t_coupon where expiry_date_stop>=?1 and expiry_date_start<=?1 and shop_id = ?2 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponBybt(Date date,Integer shopId);
	
	@Query(value="select * from t_coupon where shop_id = ?1",nativeQuery=true)
	public List<Coupon> findCouponAll(Integer shopId);
	
	
	@Query(value="select * from t_coupon where expiry_date_start>?1 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponBygtAdmin(Date date);
	
	@Query(value="select * from t_coupon where expiry_date_stop<?1 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponByltAdmin(Date date);
	
	@Query(value="select * from t_coupon where expiry_date_stop>=?1 and expiry_date_start<=?1 order by id desc",nativeQuery=true)
	public List<Coupon> findCouponBybtAdmin(Date date);
	
	@Query(value="select * from t_coupon",nativeQuery=true)
	public List<Coupon> findCouponAllAdmin();

	@Query(value="select * from t_coupon where expiry_date_stop>?1 and shop_id != ?2 and status = 1 limit 1", nativeQuery=true)
	public Coupon findRandomCoupon(Date now, Integer id);
	
}
