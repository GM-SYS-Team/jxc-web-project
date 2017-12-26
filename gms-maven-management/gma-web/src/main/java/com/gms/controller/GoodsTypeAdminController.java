package com.gms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gms.entity.jxc.GoodsType;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.GoodsTypeService;
import com.gms.service.jxc.LogService;
import com.gms.util.Constant;

/**
 * 后台管理商品类别Controller
 * @author jxc 
 *
 */
@RestController
@RequestMapping("/admin/goodsType")
public class GoodsTypeAdminController extends BaseController{

	@Resource
	private GoodsTypeService goodsTypeService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 加载商品类别树菜单
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/loadTreeInfo")
	@RequiresPermissions(value = { "商品管理"},logical=Logical.OR)
	public String loadTreeInfo(HttpServletRequest request)throws Exception{
    	logService.save(new Log(Log.SEARCH_ACTION,"查询商品类别信息")); // 写入日志
    	Shop currentShop = getCurrentShop(request);
		
		return getAllByParentId(-1,currentShop.getId(),false).toString();
	}
	
	/**
	 * 添加商品类别
	 * @param name
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	@RequiresPermissions(value = { "商品管理"},logical=Logical.OR)
	public Map<String,Object> save(String name,Integer parentId,HttpServletRequest request)throws Exception{
		
		Map<String, Object> resultMap = new HashMap<>();
		GoodsType goodsType=new GoodsType();
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			goodsType.setShopId(currentUser.getCurrentLoginShopId());
		}
		goodsType.setName(name);
		goodsType.setpId(parentId);
		goodsType.setIcon("icon-folder");
		goodsType.setState(0);
		logService.save(new Log(Log.ADD_ACTION,"添加商品类别信息"+goodsType)); 
		goodsTypeService.save(goodsType); // 保存商品类别
		
		GoodsType parentGoodsType=goodsTypeService.findById(parentId); // 查找父节点
		parentGoodsType.setState(1); // 修改state 1 根节点
		goodsTypeService.save(parentGoodsType); // 保存父节点商品类别
		
		
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 商品类别删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "商品管理"},logical=Logical.OR)
	public Map<String,Object> delete(Integer id,HttpServletRequest request)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		GoodsType goodsType=goodsTypeService.findById(id); 
		Shop currentShop = getCurrentShop(request);
		if(goodsTypeService.getAllByParentIdAndShopId(goodsType.getpId(),currentShop.getId()).size()==1){ // 假如父节点下只有当前这个子节点，修改下 父节点的state状态
			GoodsType parentGoodsType=goodsTypeService.findById(goodsType.getpId());
			parentGoodsType.setState(0); // 修改state 0  叶子节点
			goodsTypeService.save(parentGoodsType); // 保存父节点商品类别
		}
		logService.save(new Log(Log.DELETE_ACTION,"删除商品类别信息"+goodsType));  // 写入日志
		goodsTypeService.delete(id); // 删除
		resultMap.put("success", true);	
		return resultMap;
	}
	
	/**
	 * 根据父节点递归获取所有商品类别信息
	 * @param parentId
	 * @return
	 */
	public JsonArray getAllByParentId(Integer parentId,Integer shopId,boolean flag){
		JsonArray jsonArray=this.getByParentId(parentId,shopId,flag);
		for(int i=0;i<jsonArray.size();i++){
			JsonObject jsonObject=(JsonObject) jsonArray.get(i);
    		if("open".equals(jsonObject.get("state").getAsString())){
    			continue;
    		}else{
    			jsonObject.add("children", getAllByParentId(jsonObject.get("id").getAsInt(),shopId,true));
    		}
		}
		return jsonArray;
	}
	
	/**
	 * 根据父节点查询子节点
	 * @param parentId
	 * @return
	 */
	private JsonArray getByParentId(Integer parentId,Integer shopId,boolean flag){
		JsonArray jsonArray=new JsonArray();
		List<GoodsType> goodsTypeList=null;
		if(parentId==-1 && !flag){
			goodsTypeList = goodsTypeService.getAllByParentId(parentId);
		}else{
			goodsTypeList = goodsTypeService.getAllByParentIdAndShopId(parentId,shopId);
		}
		
		for(GoodsType goodsType:goodsTypeList){
			JsonObject jsonObject=new JsonObject();
			jsonObject.addProperty("id", goodsType.getId()); // 节点id
			jsonObject.addProperty("text", goodsType.getName()); // 节点名称
			if(goodsType.getState()==1){
    			jsonObject.addProperty("state", "closed"); // 根节点
    		}else{
    			jsonObject.addProperty("state", "open"); // 叶子节点
    		}
			jsonObject.addProperty("iconCls", goodsType.getIcon());
			JsonObject attributeObject=new JsonObject(); // 扩展属性
    		attributeObject.addProperty("state",goodsType.getState()); // 节点状态
			jsonObject.add("attributes", attributeObject);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	
}
