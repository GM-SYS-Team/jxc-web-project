package com.gms.dao.repository;

import java.util.List;

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
	public List<Shop> findPhoneNum(String phoneNum);

	@Query(value="select * from t_shop where id=?1 and user_id=?2", nativeQuery=true)
	public Shop queryShopByShopIdAndUserId(Integer shopId, Integer userId);

	@Query(value="select * from t_shop where user_id=?1 order by id desc", nativeQuery=true)
	public List<Shop> findByUserId(Integer userId);
}
