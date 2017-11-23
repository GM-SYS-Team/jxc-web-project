package com.gms.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.Shop;

/**
 * 用户Repository接口
 * @author jxc 
 *
 */
public interface ShopRepository extends JpaRepository<Shop, Integer>,JpaSpecificationExecutor<Shop>{

	/**
	 * 根据用户名查找用户实体
	 * @param userName
	 * @return
	 */
	@Query(value="select * from t_shop where shop_name=?1",nativeQuery=true)
	public Shop findByShopName(String shopName);

	@Query(value="select * from t_shop where phone_num=?1", nativeQuery=true)
	public Shop findPhoneNum(String phoneNum);
}
