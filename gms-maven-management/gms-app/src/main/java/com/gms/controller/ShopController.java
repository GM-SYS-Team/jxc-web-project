package com.gms.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.ShopService;
import com.gms.util.StringUtil;

@Controller
@RequestMapping("/app/shop")
@NeedAuth
public class ShopController extends BaseAppController {
	
	@Autowired
	private ShopService shopService;
	
	/**
	 * 分页查询商铺信息
	 * @param customer
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows)throws Exception{
		User user = getUser();
		Shop shop = new Shop();
		shop.setUserId(user.getId());
		List<Shop> shopList=shopService.list(shop, page, rows, Direction.ASC, "id");
		Long total=shopService.getCount(shop);
		Map<String, Object> resultMap = success();
		resultMap.put("rows", shopList);
		resultMap.put("total", total);
		return resultMap;
	}
	
	/**
	 * 添加或者修改供应商信息
	 * @param supplier
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Shop shop)throws Exception{
		User user = getUser();
		if (StringUtil.isEmpty(shop.getShopName())) {
			return error("店铺名称不能为空");
		}
		shop.setUpdateTime(new Date());
		if(shop.getId()!=null){ 
			shop.setCreateTime(new Date());
		}
		Shop existedNameOne = shopService.findByShopName(shop.getShopName());
		if(existedNameOne!=null){
			return error("该商铺名称已存在");
		}
		shop.setUserId(user.getId());
		shopService.save(shop);			
		return success();
	}
	
	
	/**
	 * 删除供应商信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		if (StringUtils.isEmpty(ids)) {
			return error("非法请求");
		}
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			shopService.delete(id);							
		}
		return success();
	}
}
