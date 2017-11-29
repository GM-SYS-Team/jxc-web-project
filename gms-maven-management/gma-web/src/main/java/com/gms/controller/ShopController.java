package com.gms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.ShopService;

@RestController
@RequestMapping("/admin/shop")
public class ShopController extends BaseController {
	/**
	 * @author zhoutianqi
	 * @className ShopController.java
	 * @date 2017年11月21日 下午5:24:12
	 * @description
	 */
	@Resource
	private LogService logService;
	@Autowired
	private ShopService shopService;

	/**
	 * 分页查询商铺信息
	 * 
	 * @param customer
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(Shop shop,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows,
			HttpServletRequest request,
			@RequestParam(value = "userId", required = false) Integer userId)
			throws Exception {
		shop.setUserId(userId);
		List<Shop> shopList = shopService.list(shop, page, rows, Direction.ASC,
				"id");
		Long total = shopService.getCount(shop);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", shopList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION, "查询商铺信息")); // 写入日志
		return resultMap;
	}

	/**
	 * 添加或者修改供应商信息
	 * 
	 * @param supplier
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(Shop shop, HttpServletRequest request)
			throws Exception {
		boolean flag = true;
		shop.setUpdateTime(new Date());
		Map<String, Object> resultMap = new HashMap<>();
		Shop existedNameOne = shopService.findByShopName(shop.getShopName());
		List<Shop> existedPhoneOne = shopService.findPhoneNum(shop
				.getPhoneNum());
		if (shop.getId() != null) { // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION, "更新商铺信息" + shop));
			Shop old_shop = shopService.findById(shop.getId());
			if (!shop.getPhoneNum().equals(old_shop.getPhoneNum())) {
				if (existedPhoneOne.size() > 0) {
					resultMap.put("error", "该手机号码已存在");
					flag = false;
				}
			}
			if(!shop.getShopName().equals(old_shop.getShopName())){
				if (existedNameOne != null) {
					resultMap.put("error", "该商铺名称已存在");
					flag = false;
				} 
			}
		} else {
			if (existedNameOne != null) {
				resultMap.put("error", "该商铺名称已存在");
				flag = false;
			} else if (existedPhoneOne.size() > 0) {
				resultMap.put("error", "该手机号码已存在");
				flag = false;
			} else {
				logService.save(new Log(Log.ADD_ACTION, "添加商铺信息" + shop));
				shop.setCreateTime(new Date());
			}
		}
		if (flag) {
			shopService.save(shop);
			resultMap.put("success", true);
		}
		return resultMap;
	}

	/**
	 * 删除供应商信息
	 * 
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String, Object> delete(String ids) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			int id = Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION, "删除商铺信息"
					+ shopService.findById(id))); // 写入日志
			shopService.delete(id);
		}
		resultMap.put("success", true);
		return resultMap;
	}

	@PostMapping("/currentMap")
	public Map<String, Object> getCurrentMap(HttpServletRequest request) {
		Shop shop = getCurrentShop(request);
		User user = getCurrentUser(request);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("shop", shop);
		resultMap.put("user", user);
		return resultMap;
	}

}
