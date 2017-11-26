package com.gms.service.jxc;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;

/**
 * 商铺Service接口
 * @author jxc 
 *
 */
public interface ShopService {

	/**
	 * 根据商铺名查找商铺实体
	 * @param shopName
	 * @return
	 */
	public Shop findByShopName(String shopName);
	
	/**
	 * 根据id查询商铺实体
	 * @param id
	 * @return
	 */
	public Shop findById(Integer id);
	
	/**
	 * 修改或者修改商铺信息
	 * @param Shop
	 */
	public void save(Shop shop);
	
	/**
	 * 根据条件分页查询商铺信息
	 * @param Shop
	 * @param page
	 * @param pageSize
	 * @param direction
	 * @param properties
	 * @return
	 */
	public List<Shop> list(Shop user,Integer page,Integer pageSize,Direction direction,String... properties);
	
	/**
	 * 获取总记录数
	 * @param user
	 * @return
	 */
	public Long getCount(Shop user);
	
	/**
	 * 根据id删除商铺
	 * @param id
	 */
	public void delete(Integer id);
	
	
	/**
	 * 根据手机号查询商铺
	 * @param telephone
	 * @return
	 */
	public Shop findPhoneNum(String phoneNum);
	
	public List<Shop> findByUserId(Integer userId);
}
