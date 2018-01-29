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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gms.entity.jxc.Customer;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.CustomerService;
import com.gms.service.jxc.LogService;
import com.gms.util.Constant;

/**
 * 后台管理客户Controller
 * @author jxc 
 *
 */
@RestController
@RequestMapping("/admin/customer")
public class CustomerAdminController extends BaseController{
	
	@Resource
	private CustomerService customerService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 分页查询客户信息
	 * @param customer
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String,Object> list(Customer customer,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows,
			HttpServletRequest request)throws Exception{
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			customer.setShopId(currentUser.getCurrentLoginShopId());
		}
		List<Customer> customerList=customerService.list(customer, page, rows, Direction.ASC, "id");
		Long total=customerService.getCount(customer);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", customerList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询客户信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 下拉框模糊查询
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/comboList")
	public List<Customer> comboList(HttpServletRequest request,String q)throws Exception{
		if(q==null){
			q="";
		}
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			return customerService.findByShopAndName(currentUser.getCurrentLoginShopId(),"%"+q+"%");
		}
		return customerService.findByName("%"+q+"%");
	}
	
	
	/**
	 * 添加或者修改客户信息
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String,Object> save(Customer customer,HttpServletRequest request)throws Exception{
		if(customer.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新客户信息"+customer)); 
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加客户信息"+customer)); 
		}
		User currentUser = getCurrentUser(request);
		if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
			customer.setShopId(currentUser.getCurrentLoginShopId());
		}
		Map<String, Object> resultMap = new HashMap<>();
		customerService.save(customer);			
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除客户信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(String ids)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		String []idsStr=ids.split(",");
		for(int i=0;i<idsStr.length;i++){
			int id=Integer.parseInt(idsStr[i]);
			logService.save(new Log(Log.DELETE_ACTION,"删除客户信息"+customerService.findById(id)));  // 写入日志
			customerService.delete(id);							
		}
		resultMap.put("success", true);
		return resultMap;
	}

}