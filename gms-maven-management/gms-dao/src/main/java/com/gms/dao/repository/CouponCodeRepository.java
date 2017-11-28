package com.gms.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.CouponCode;


/** 
* @ClassName: CouponGoodsRepository 
* @Description: TODO
* @author Mr.Qian
* @date 2017年11月21日 下午3:07:17  
*/
public interface CouponCodeRepository extends JpaRepository<CouponCode, Integer>,JpaSpecificationExecutor<CouponCode>{

	@Query(value="select * from t_coupon_code where coupon_id = ?1", nativeQuery=true)
	public List<CouponCode> findCouponCodeListByCouponId(Integer couponId); 
	
	@Query(value="select count(*) as amount,receive_time from t_coupon_code  where coupon_id in(select id from t_coupon where shop_id=?1) and receive_time>=?2 group by receive_time",nativeQuery=true)
	public List<CouponCode> queryCouponCodeByReTime(Integer shopId,Date receiveDate); 
	
}
