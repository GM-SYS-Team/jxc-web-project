package com.gms.controller;

import javax.servlet.http.HttpServletRequest;

import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;

public class BaseController {
	/**
	 * 获取Session中保存的当前User Info
	*/
	public User getCurrentUser(HttpServletRequest request){
		User currentUser= (User)request.getSession().getAttribute("currentUser");
		return currentUser;
	}
	
	/**
	 * 获取Session中保存的当前Shop Info
	*/
	public Shop getCurrentShop(HttpServletRequest request){
		Shop currentShop= (Shop)request.getSession().getAttribute("currentShop");
		return currentShop;
	}
}
