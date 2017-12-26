package com.gms.service.jxc;

import java.util.Date;
import java.util.List;

import com.gms.entity.jxc.CouponCode;
import com.gms.entity.jxc.Shop;

/**
 * @ClassName: CouponCodeService
 * @Description:
 * @author shenlinli
 * @date 2017年11月22日 上午11:41:18
 */
public interface CouponCodeService {

	
	public List<CouponCode> queryCouponCodeList(Shop shop,Date receiveDate);

	public List<CouponCode> findListByUserId(Integer id, Integer status);

	public void save(CouponCode couponCode);

	public CouponCode findCouponCodeById(Integer couponCodeId, Integer ownerId);


}