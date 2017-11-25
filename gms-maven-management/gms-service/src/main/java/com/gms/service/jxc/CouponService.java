package com.gms.service.jxc;

import java.util.List;

import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponGoods;

/**
 * @ClassName: CouponService
 * @Description: 优惠券Service接口
 * @author shenlinli
 * @date 2017年11月22日 上午11:41:18
 */
public interface CouponService {

	/** 
	* @Title: save 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param coupon    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void save(Coupon coupon);

	/** 
	* @Title: findCouponAll 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @return List<Coupon>    返回类型 
	* @throws 
	*/
	public List<Coupon> findCouponAll(Integer shopId);

	/** 
	* @Title: findCouponByStatus 
	* @Description: TODO(这里用一句话描述这个方法的作用) 1-未开始  2-进行中  3-已过期
	* @param @param status    设定文件 
	* @return List<Coupon>    返回类型 
	* @throws 
	*/
	public List<Coupon> findCouponByStatus(Integer status,Integer shopId);
	
	/** 
	* @Title: saveCouponGoods 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param couponGoods    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void saveCouponGoods(CouponGoods couponGoods);
	
	/** 
	* @Title: deleteCoupon 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param id    设定文件 
	* @return void    返回类型 
	* @throws 
	*/
	public void deleteCoupon(Integer id,Integer shopId);

}
