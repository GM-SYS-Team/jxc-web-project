package com.gms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Goods;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.GoodsService;
import com.gms.service.jxc.LogService;
import com.gms.util.Constant;
import com.gms.util.StringUtil;

/**
 * 后台管理商品Controller
 * @author jxc 
 *
 */
@RestController
@RequestMapping("/admin/goods")
public class GoodsAdminContrller extends BaseController{

	@Resource
	private GoodsService goodsService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 根据条件分页查询商品信息
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	@RequiresPermissions(value = { "商品管理"},logical=Logical.OR)
	public Map<String,Object> list(Goods goods,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows,
			HttpServletRequest request)throws Exception{
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			goods.setShopId(currentUser.getCurrentLoginShopId());
		}
		Map<String, Object> resultMap = new HashMap<>();
		List<Goods> goodsList=goodsService.list(goods, page, rows, Direction.ASC, "id");
		Long total=goodsService.getCount(goods);
		resultMap.put("rows", goodsList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询商品信息")); // 写入日志
		return resultMap;
	}
	
	
	
	/**
	 * 生成商品编码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/genGoodsCode")
	@RequiresPermissions(value = { "商品管理" })
	public String genGoodsCode()throws Exception{
		String maxGoodsCode=goodsService.getMaxGoodsCode();
		if(StringUtil.isNotEmpty(maxGoodsCode)){
			Integer code = Integer.valueOf(maxGoodsCode)+1;
			String codes = code.toString();
			int length = codes.length();
			for (int i = 4; i > length; i--) {
				codes = "0"+codes;
			}
			return codes;
		}else{
			return "0001";
		}
	}
	
	/**
	 * 添加商品
	 * @param goods
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "商品管理"},logical=Logical.OR)
	public Map<String,Object> save(Goods goods,HttpServletRequest request)throws Exception{
		if(goods.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新商品信息"+goods)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加商品信息"+goods)); 
			goods.setLastPurchasingPrice(goods.getPurchasingPrice()); // 设置上次进价为当前价格
		}
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			goods.setShopId(currentUser.getCurrentLoginShopId());
		}
		Map<String, Object> resultMap = new HashMap<>();
		goodsService.save(goods);
		resultMap.put("success", true);	
		return resultMap;
	}
	
	
	/**
	 * 删除商品信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "商品管理" })
	public Map<String,Object> delete(Integer id)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Goods goods=goodsService.findById(id);
		if(goods.getState()==1){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该商品已经期初入库，不能删除！");
		}else if(goods.getState()==2){
			resultMap.put("success", false);
			resultMap.put("errorInfo", "该商品已经发生单据，不能删除！");
		}else{
			logService.save(new Log(Log.DELETE_ACTION,"删除商品信息"+goodsService.findById(id)));  // 写入日志
			goodsService.delete(id);							
			resultMap.put("success", true);			
		}
		return resultMap;
	}
}