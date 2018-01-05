package com.gms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Role;
import com.gms.entity.jxc.User;
import com.gms.entity.jxc.UserRole;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.RoleService;
import com.gms.service.jxc.UserRoleService;
import com.gms.service.jxc.UserService;
import com.gms.util.MD5Util;
import com.gms.util.StringUtil;
import com.gms.util.UUIDUtil;

/**
 * 后台管理用户Controller
 * @author jxc 
 *
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

	@Resource
	private UserService userService;
	
	@Resource
	private RoleService roleService;
	    
	@Resource
	private UserRoleService userRoleService;
	
	@Resource
	private LogService logService;
	
	/**
	 * 修改密码
	 * @param id
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping("/modifyPassword")
	public Map<String,Object> modifyPassword(Integer id,String newPassword,HttpSession session)throws Exception{
		User currentUser=(User) session.getAttribute("currentUser");
		User user=userService.findById(currentUser.getId());
		user.setPassword(MD5Util.encode(newPassword));
		user.setUpdateTime(new Date());
		userService.save(user);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"修改密码")); // 写入日志
		return map;
	}
	
	/**
	 * 安全退出
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/logout")
	public String logout(HttpSession session)throws Exception{
		session.removeAttribute("currentShop");
		session.removeAttribute("currentUser");
		logService.save(new Log(Log.LOGOUT_ACTION,"用户注销"));
		SecurityUtils.getSubject().logout();
		return "redirect:/login.html";
	}
	
	/**
	 * 分页查询用户信息
	 * @param user
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> list(User user,@RequestParam(value="page",required=false)Integer page,@RequestParam(value="rows",required=false)Integer rows,@RequestParam(value="shopid",required=false)Integer shopid)throws Exception{
		if(shopid!=null){
			if(user==null){user=new User();}
			user.setCurrentLoginShopId(shopid);
		}
		List<User> userList=userService.list(user, page, rows, Direction.ASC, "id");
		for(User u:userList){
			List<Role> roleList=roleService.findByUserId(u.getId());
			StringBuffer sb=new StringBuffer();
			for(Role r:roleList){
				sb.append(","+r.getName());
			}
			u.setRoles(sb.toString().replaceFirst(",", ""));
		}
		Long total=userService.getCount(user);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", userList);
		resultMap.put("total", total);
		logService.save(new Log(Log.SEARCH_ACTION,"查询用户信息")); // 写入日志
		return resultMap;
	}
	
	/**
	 * 保存用户角色设置
	 * @param roleIds
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/saveRoleSet")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> saveRoleSet(String roleIds,Integer userId)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		userRoleService.deleteByUserId(userId);  // 根据用户id删除所有用户角色关联实体
		if(StringUtil.isNotEmpty(roleIds)){
			String idsStr[]=roleIds.split(",");
			for(int i=0;i<idsStr.length;i++){ // 然后添加所有用户角色关联实体
				UserRole userRole=new UserRole();
				userRole.setUser(userService.findById(userId));
				userRole.setRole(roleService.findById(Integer.parseInt(idsStr[i])));
				userRoleService.save(userRole);
			}
		}
		resultMap.put("success", true);
		logService.save(new Log(Log.UPDATE_ACTION,"保存用户角色设置")); 
		return resultMap;
	}
	
	
	/**
	 * 添加或者修改用户信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> save(User user)throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		//不管更新还是新增，名称都不能重复
		if(user.getId()==null){
			if(userService.findByUserName(user.getUserName())!=null){
				resultMap.put("success", false);
				resultMap.put("errorInfo", "用户名已经存在!");
				return resultMap;
			}
		}
		User existUser = null;
		if(user.getId()!=null){ // 写入日志
			logService.save(new Log(Log.UPDATE_ACTION,"更新用户信息"+user)); 
			existUser = userService.findById(user.getId());
			existUser.setPassword(MD5Util.encode(user.getPassword()));
			existUser.setRemarks(user.getRemarks());
			existUser.setTrueName(user.getTrueName());
			existUser.setUserType(user.getUserType());
			existUser.setUpdateTime(new Date());
			userService.save(existUser);	
		}else{
			logService.save(new Log(Log.ADD_ACTION,"添加用户信息"+user));
			user.setCreateTime(new Date());
			user.setUuid(UUIDUtil.getUUIDKey());
			//新增、修改操作密码和更新时间都会改变
			user.setPassword(MD5Util.encode(user.getPassword()));
			user.setUpdateTime(new Date());
			userService.save(user);	
		}
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	/**
	 * 删除用户信息
	 * @param id
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String,Object> delete(Integer id)throws Exception{
		logService.save(new Log(Log.DELETE_ACTION,"删除用户信息"+userService.findById(id)));  // 写入日志
		Map<String, Object> resultMap = new HashMap<>();
		userRoleService.deleteByUserId(id); // 删除用户角色关联信息
		userService.delete(id);				
		resultMap.put("success", true);
		return resultMap;
	}
}