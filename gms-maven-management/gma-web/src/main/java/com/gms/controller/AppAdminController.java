package com.gms.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.gms.conf.ImageServerProperties;
import com.gms.entity.jxc.App;
import com.gms.entity.jxc.Log;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.AppService;
import com.gms.service.jxc.LogService;
import com.gms.util.HttpsUtil;
import com.gms.util.StringUtil;

@RestController
@RequestMapping("/admin/app")
public class AppAdminController extends BaseController{
	/**
	 * @author zhoutianqi
	 * @className AppController.java
	 * @date 2018年1月17日 下午4:00:55
	 * @description 
	 */
	private static final Logger logger = LoggerFactory.getLogger(AppAdminController.class);
	@Resource
	private LogService logService;
	@Autowired
	private AppService appService;
	@Autowired
	private ImageServerProperties imageServerProperties;
	
	@PostMapping("/saveAndUpload")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String, Object> saveAndUpload(App app,MultipartFile file,HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		if(file==null){
			resultMap.put("error", true);
			resultMap.put("errorInfo", "上传客户端文件不能为空");
			return resultMap;
		}
		User user = getCurrentUser(request);
		logService.save(new Log(Log.ADD_ACTION, "上传APP,操作用户--->"+user.getId())); // 写入日志
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("picType", app.getAppType());//用户头像
		if(file!=null){
			String result = HttpsUtil.getInstance().sendHttpPost(imageServerProperties.getUrl()+"/"+
					imageServerProperties.getAction(), file,paramMap);
			if(result!=null){
				String fileAddress = null;
				JSONObject resultJson = (JSONObject)JSONObject.parse(result);
				if(resultJson.getString("message").equals("Ok")){
					fileAddress = resultJson.getJSONObject("data").getString("url");
					app.setAppUrl(fileAddress);
				}
			}
		}
		if(StringUtil.isEmpty(app.getAppUrl())){
			resultMap.put("error", true);
			resultMap.put("errorInfo", "服务器请假了，请稍后重试");
			return resultMap;
		}
		app.setCreateTime(new Date());
		appService.save(app);
		resultMap.put("success", true);
		return resultMap;
	}
	
	
	@PostMapping("/modify")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String, Object> save(App app,HttpServletRequest request) throws Exception {
		/* 当前登录的店铺 */
		User user = getCurrentUser(request);
		logService.save(new Log(Log.ADD_ACTION, "上传APP,操作用户--->"+user.getId())); // 写入日志
		App previousApp = appService.findById(app.getId());
		previousApp.setAppName(app.getAppName());
		previousApp.setAppType(app.getAppType());
		previousApp.setAppVersion(app.getAppVersion());
		previousApp.setRemarks(app.getRemarks());
		appService.save(app);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		return resultMap;
	}

	@PostMapping("/list")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String, Object> list(App app,
			HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "rows", required = false) Integer rows)
			throws Exception {
		/* 当前登录的店铺 */
		List<App> appListResult = appService.list(app, page,
				rows, Direction.DESC, "createTime");
		Long appListSize = appService.getCount(app);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("rows", appListResult);
		resultMap.put("total", appListSize);
		logService.save(new Log(Log.SEARCH_ACTION, "app list操作")); // 写入日志
		return resultMap;
	}
	
	
	@PostMapping("/delete")
	@RequiresPermissions(value = { "角色管理" })
	public Map<String, Object> delete(Integer id,HttpServletRequest request) throws Exception {
		/* 当前登录的店铺 */
		User user = getCurrentUser(request);
		logService.save(new Log(Log.DELETE_ACTION, "删除APP,操作用户--->"+user.getId())); // 写入日志
		appService.delete(id);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		return resultMap;
	}
}
