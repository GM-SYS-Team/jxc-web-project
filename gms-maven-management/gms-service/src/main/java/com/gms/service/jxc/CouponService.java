package com.gms.service.jxc;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.gms.entity.jxc.Coupon;
import com.gms.entity.jxc.CouponCode;
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

	/**
	 * 根据商商品ID查询是否存在已关联发放的优惠券
	 * @param goodsId
	 * @return
	 */
	public List<CouponGoods> findCouponListByGoodsId(Integer goodsId);

	/**
	 * 根据优惠券id查询对应的优惠券
	 * @param couponId
	 * @return
	 */
	public Coupon findCouponById(Integer couponId);

	/**
	 * 根据优惠券ID查询是否已经发放了优惠券
	 * @param couponId
	 * @return
	 */
	public List<CouponCode> findListByCouponId(Integer couponId);
	
	/**
	 * 根据优惠券编码或者名称分页查询没有库存的商品信息
	 * @param codeOrName
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Coupon> list(Coupon coupon,Integer page,Integer pageSize,Direction direction,Integer state, String... properties);
	public Long listCount(Coupon coupon,Integer state);

}