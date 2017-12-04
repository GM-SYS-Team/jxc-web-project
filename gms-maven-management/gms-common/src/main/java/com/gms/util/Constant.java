package com.gms.util;

public class Constant {
	public static final String DEFAULT_TABLE_CODE="00000001";
	//管理员用户类别
	public static final String ROOTTYPE = "0";
	//商户用户类别
	public static final String SHOPTYPE = "1";
	
	//用户未登录状态码
	public static final String RET_CODE_NOT_LOGIN_EXCEPTION = "1000";
	//用户未登录提示信息
	public static final String RET_CODE_NOT_LOGIN_EXCEPTION_MSG = "用户未登录";
	//系统其它异常状态码
	public static final String RET_CODE_ERRO_SYS_EXCEPTION = "-1000";
	//订单状态 已支付
	public static final Integer ORDER_PAYED_STATE = 1;
	//订单状态 未支付
	public static final Integer ORDER_NO_PAYED_STATE = 2;
	
	//接口成功状态码
	public static final String RET_CODE_SUCCESS = "1";
	//接口失败状态码
	public static final String RET_CODE_ERROR = "0";
	
	public static final String RET_CODE_ERRO_SYS_EXCEPTION_MSG = "系统异常";
}