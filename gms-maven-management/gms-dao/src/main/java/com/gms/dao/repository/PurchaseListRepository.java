package com.gms.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.gms.entity.jxc.PurchaseList;

/**
 * 进货单Repository接口
 * @author Administrator
 *
 */
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Integer>,JpaSpecificationExecutor<PurchaseList>{

	/**
	 * 获取当天最大进货单号
	 * @return
	 */
	@Query(value="SELECT MAX(purchase_number) FROM t_purchase_list WHERE TO_DAYS(purchase_date) = TO_DAYS(NOW())",nativeQuery=true)
	public String getTodayMaxPurchaseNumber();
	
	@Query(value="SELECT count(*) FROM t_purchase_list WHERE shop_id=?1 and state=?2",nativeQuery=true)
	public int getPurchasePayedListCount(Integer shopId,Integer state);
	
	@Query(value="SELECT count(*) FROM t_purchase_list WHERE state=?1",nativeQuery=true)
	public int getPurchasePayedListCountAdmin(Integer state);
}
