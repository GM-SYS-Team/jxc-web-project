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
	
	@Query(value="select count(*) as amount,receive_time from t_coupon_code  where receive_time>=?1 group by receive_time",nativeQuery=true)
	public List<CouponCode> queryCouponCodeByReTimeAdmin(Date receiveDate);

	@Query(value="select * from t_coupon_code where user_id = ?1 order by id desc", nativeQuery=true)
	public List<CouponCode> findCouponAll(Integer userId);
	
	/**
	 * 已过期
	 * @param userId
	 * @param today
	 * @return
	 */
	@Query(value="select * form t_coupon_code where user_id = ?1 and expiry_date_stop > ?2 order by id desc", nativeQuery=true)
	public List<CouponCode> findCouponBygtNow(Integer userId, Date today);

	/**
	 * 未使用
	 * @param userId
	 * @param today
	 * @return
	 */
	@Query(value="select * from t_coupon_code where user_id = ?1 and expiry_date_stop < ?2 and is_used = '0' order by id desc", nativeQuery=true)
	public List<CouponCode> findNotUseCoupon(Integer userId, Date today);
	
	/**
	 * 已使用
	 * @param userId
	 * @param today
	 * @return
	 */
	@Query(value="select * from t_coupon_code where user_id = ?1 and is_used = '1' order by id desc", nativeQuery=true)
	public List<CouponCode> findUsedCoupon(Integer userId);

	@Query(value="select * from t_coupon_code where id = ?1 and user_id = ?2", nativeQuery=true)
	public CouponCode findCouponCodeById(Integer couponCodeId, Integer ownerId); 
	
}
