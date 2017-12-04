package com.gms.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.DamageList;

/**
 * 报损单Repository接口
 * @author Administrator
 *
 */
public interface DamageListRepository extends JpaRepository<DamageList, Integer>,JpaSpecificationExecutor<DamageList>{

	/**
	 * 获取当天最大报损单号
	 * @return
	 */
	@Query(value="SELECT MAX(damage_number) FROM t_damage_list WHERE TO_DAYS(damage_date) = TO_DAYS(NOW())",nativeQuery=true)
	public String getTodayMaxDamageNumber();
	
	/**
	 * 报损订单总数
	 * @return
	 */
	@Query(value="SELECT count(*) FROM t_damage_list WHERE shop_id=?1",nativeQuery=true)
	public int getDamageOrderListCount(Integer shopId);
}
