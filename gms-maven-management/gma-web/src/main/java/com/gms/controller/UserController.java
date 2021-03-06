package com.gms.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.Menu;
import com.gms.entity.jxc.Role;
import com.gms.entity.jxc.Shop;
import com.gms.entity.jxc.User;
import com.gms.entity.util.ResultData;
import com.gms.service.jxc.LogService;
import com.gms.service.jxc.MenuService;
import com.gms.service.jxc.RoleService;
import com.gms.service.jxc.ShopService;
import com.gms.service.jxc.UserService;
import com.gms.util.Constant;
import com.gms.util.MD5Util;
import com.gms.util.StringUtil;

/**
 * 当前登录用户控制器
 * @author jxc 
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	private static Logger logger = Logger.getLogger(UserController.class);
	@Resource
	private RoleService roleService;
	
	@Resource
	private UserService userService;
	@Autowired
	private ShopService shopService;
	
	@Resource
	private MenuService menuService;
	
	@Resource
	private LogService logService;
	/*//文件存储路径
	@Value(value = "${picuploadPath}")
	private String picturePath;*/
	/**
     * 用户登录请求
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String,Object> login(String imageCode,@Valid User user,BindingResult bindingResult,HttpSession session){
    	Map<String,Object> map=new HashMap<String,Object>();
    	if(StringUtil.isEmpty(imageCode)){
    		map.put("success", false);
    		map.put("errorInfo", "请输入验证码！");
    		return map;
    	}
    	if(!session.getAttribute("checkcode").equals(imageCode)){
    		map.put("success", false);
    		map.put("errorInfo", "验证码输入错误！");
    		return map;
    	}
    	if(bindingResult.hasErrors()){
    		map.put("success", false);
    		map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
    		return map;
    	}
		Subject subject=SecurityUtils.getSubject();
		UsernamePasswordToken token=new UsernamePasswordToken(user.getUserName(), MD5Util.encode(user.getPassword()));
		try{
			subject.login(token); // 登录认证
			String userName=(String) SecurityUtils.getSubject().getPrincipal();
			User currentUser=userService.findByUserName(userName);
			List<Shop> shopList = shopService.findByUserId(currentUser.getId());
			if(currentUser.getUserType().equals(Constant.SHOPTYPE) && shopList.isEmpty()){
				map.put("success", false);
	    		map.put("errorInfo", "该用户名下未注册商铺");
	    		return map;
			}
 			if(currentUser.getUserType().equals(Constant.SHOPTYPE)){
				Shop shop =null;
				if(currentUser.getCurrentLoginShopId()!=null){
					shop = shopService.findById(currentUser.getCurrentLoginShopId());
				}else{
					shop = shopList.get(0);//默认进入第一家店铺
					currentUser.setCurrentLoginShopId(shop.getId());
					userService.save(currentUser);
				}
				
				session.setAttribute("currentShop", shop);
				currentUser.setShopList(shopList);//缓存店铺信息
			}
			session.setAttribute("currentUser", currentUser);
			List<Role> roleList=roleService.findByUserId(currentUser.getId());
			map.put("roleList", roleList);
			map.put("roleSize", roleList.size());
			map.put("success", true);
			if(currentUser.getUserType().equals(Constant.ROOTTYPE)){
				map.put("userType", "root");
			}else{
				map.put("userType", "shop");
			}
			logService.save(new Log(Log.LOGIN_ACTION,"用户登录")); // 写入日志
			return map;
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", false);
			map.put("errorInfo", "用户名或者密码错误！");
			return map;
		}
    }
    
    /**
     * 保存角色信息
     * @param roleId
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/saveRole")
    public Map<String,Object> saveRole(Integer roleId,HttpSession session)throws Exception{
    	Map<String,Object> map=new HashMap<String,Object>();
    	Role currentRole=roleService.findById(roleId);
    	session.setAttribute("currentRole", currentRole); // 保存当前角色信息
    	map.put("success", true);
    	return map;
    }
    
    /**
     * 加载当前用户信息
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/loadUserInfo")
    public String loadUserInfo(HttpSession session)throws Exception{
    	User currentUser=(User) session.getAttribute("currentUser");
    	Role currentRole=(Role) session.getAttribute("currentRole");
    	return "欢迎您："+currentUser.getTrueName()+"&nbsp;[&nbsp;"+currentRole.getName()+"&nbsp;]";
    }
    
    /**
     * 加载权限菜单
     * @param session
     * @return
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/loadMenuInfo")
    public String loadMenuInfo(HttpSession session,Integer parentId)throws Exception{
    	Role currentRole=(Role) session.getAttribute("currentRole");
    	return getAllMenuByParentId(parentId,currentRole.getId()).toString();
    }
    
    /**
     * 获取所有菜单信息
     * @param parentId
     * @param roleId
     * @return
     */
    private JsonArray getAllMenuByParentId(Integer parentId,Integer roleId){
    	JsonArray jsonArray=this.getMenuByParentId(parentId, roleId);
    	for(int i=0;i<jsonArray.size();i++){
    		JsonObject jsonObject=(JsonObject) jsonArray.get(i);
    		if("open".equals(jsonObject.get("state").getAsString())){
    			continue;
    		}else{
    			jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt(),roleId));
    		}
    	}
    	return jsonArray;
    }
    
    /**
     * 根据父节点和用户角色id查询菜单
     * @param parentId
     * @param roleId
     * @return
     */
    private JsonArray getMenuByParentId(Integer parentId,Integer roleId){
    	List<Menu> menuList=menuService.findByParentIdAndRoleId(parentId, roleId);
    	JsonArray jsonArray=new JsonArray();
    	for(Menu menu:menuList){
    		JsonObject jsonObject=new JsonObject();
    		jsonObject.addProperty("id", menu.getId()); // 节点id
    		jsonObject.addProperty("text", menu.getName()); // 节点名称
    		if(menu.getState()==1){
    			jsonObject.addProperty("state", "closed"); // 根节点
    		}else{
    			jsonObject.addProperty("state", "open"); // 叶子节点
    		}
    		jsonObject.addProperty("iconCls", menu.getIcon());
    		JsonObject attributeObject=new JsonObject(); // 扩展属性
    		attributeObject.addProperty("url", menu.getUrl()); // 菜单请求地址
			jsonObject.add("attributes", attributeObject);
			jsonArray.add(jsonObject);
    	}
    	return jsonArray;
    }
    
    /*//文件上传相关代码
    @ResponseBody
    @RequestMapping(value = "picture/upload")
    public ResultData upload(@RequestParam("pictureFile") MultipartFile pictureFile) {
        if (pictureFile.isEmpty()) {
            return ResultData.forbidden().putDataValue("message", "文件不能为空");
        }
        // 获取文件名
        String fileName = pictureFile.getOriginalFilename();
        logger.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File dest = new File(picturePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
        	pictureFile.transferTo(dest);
            return ResultData.ok().putDataValue("message", "头像上传成功");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResultData.serverInternalError().putDataValue("message", "服务器请假了，请稍后再试");
    }*/
}