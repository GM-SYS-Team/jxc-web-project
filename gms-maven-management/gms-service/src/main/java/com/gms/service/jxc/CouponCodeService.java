package com.gms.service.jxc;

import java.util.Date;
import java.util.List;

import com.gms.entity.jxc.CouponCode;

/**
 * @ClassName: CouponCodeService
 * @Description:
 * @author shenlinli
 * @date 2017年11月22日 上午11:41:18
 */
public interface CouponCodeService {

	
	public List<CouponCode> queryCouponCodeList(Integer shopId,Date receiveDate);


}