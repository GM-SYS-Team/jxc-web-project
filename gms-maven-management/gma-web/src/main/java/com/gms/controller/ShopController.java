package com.gms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.ShopService;
import com.gms.util.Constant;
import com.gms.util.HttpsUtil;
import com.gms.util.StringUtil;

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
	@Autowired
	private ImageServerProperties imageServerProperties;

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
		shop.setUserId(userId);//优先考虑入参userid
		//获取当前用户，判断是管理员操作还是商铺操作
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){//如果是商铺账户，只取当前账户的商铺列表
			shop.setUserId(currentUser.getId());
		}
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
	 * 添加或者修改供应商信息  管理员与商户公用逻辑
	 * 
	 * @param supplier
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(Shop shop, HttpServletRequest request,@RequestParam(value = "pictureFile", required = false) MultipartFile pictureFile)
			throws Exception {
		boolean flag = true;
		Map<String, Object> resultMap = new HashMap<>();
		//获取当前用户，判断是管理员操作还是商铺操作
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			shop.setUserId(currentUser.getId());
		}
		//选择了图像文件才会上传，否则用老的发黄的旧照片
		if(pictureFile!=null && StringUtil.isValid(pictureFile.getOriginalFilename())){
			String result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl()+"/"+
					imageServerProperties.getAction(), pictureFile);
			if(result!=null){
				String imageName = null;
				JSONObject resultJson = (JSONObject)JSONObject.parse(result);
				if(resultJson.getString("message").equals("Ok")){
					imageName = resultJson.getJSONObject("data").getString("imageName");
					shop.setPictureAddress(imageName);
				}else{
					resultMap.put("error", "服务器请假了，请稍后重试");
					flag = false;
				}
			}
		}
		
		shop.setUpdateTime(new Date());
		Shop existedNameOne = shopService.findByShopName(shop.getShopName());
		List<Shop> existedPhoneOne = shopService.findPhoneNum(shop
				.getPhoneNum());
		if (shop.getId() != null) { // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION, "更新商铺信息" + shop));
			Shop old_shop = shopService.findById(shop.getId());
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
	
	/**
	 * 删除供应商信息
	 * 
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/createAndUpdateQuickMark")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String, Object> createAndUpdateQuickMark(Integer id) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		String result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl()+"/"+
				imageServerProperties.getQuickMarkAction(),"quickMarkStr="+String.valueOf(id.intValue()));
		Shop shop = shopService.findById(id);
		if(result!=null){
			String quickMarkImageName = null;
			JSONObject resultJson = (JSONObject)JSONObject.parse(result);
			if(resultJson.getString("message").equals("Ok")){
				quickMarkImageName = resultJson.getJSONObject("data").getString("quickMark");
				shop.setQuickMark(quickMarkImageName);
				shopService.save(shop);
			}else{
				resultMap.put("error", "服务器请假了，请稍后重试");
				return resultMap;
			}
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
	
	/**
	 * 商铺切换，重置session中的shop信息 
	 * 
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/exchangeShop")
	@RequiresPermissions(value = { "供应商管理" })
	public Map<String, Object> exchangeShop(@RequestParam(value = "shopid", required = true)Integer shopid,HttpSession session) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			session.setAttribute("currentShop", shopService.findById(shopid));
			resultMap.put("success", true);
		} catch (Exception e) {
			resultMap.put("success", false);
			resultMap.put("errorInfo", "系统异常，请联系系统管理员");
		}
		
		return resultMap;
	}
}
