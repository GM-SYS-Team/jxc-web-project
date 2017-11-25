package com.gms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.UserService;
import com.gms.util.MD5Util;
import com.gms.util.StringUtil;

/**
 * 当前登录用户控制器
 * @author jxc 
 *
 */
@Controller
@RequestMapping("/app/user")
public class UserController extends BaseAppController {
	private static Logger logger = Logger.getLogger(UserController.class);
	
	@Resource
	private UserService userService;
	
	//文件存储路径
	@Value(value = "${picuploadPath}")
	private String picturePath;
	/**
     * 用户登录请求
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/login")
    public Map<String,Object> login(String password, String telephone){
    	if (StringUtils.isEmpty(telephone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(password)) {
    		return error("密码不能为空");
    	}
    	User user = userService.findUserByTelephone(telephone);
    	if (user == null) {
			return error("该手机号尚未注册");
    	}
    	if (!user.getPassword().equals(MD5Util.encode(password))) {
    		return error("密码错误");
    	}
    	cacheUser(user);
    	Map<String,Object> map = success();
    	map.put("data", user);
    	return map;
    }
    
    /**
     * 获取用户信息
     * @param userId
     * @return
     */
    @ResponseBody
    @PostMapping("/getUserInfo/{token}")
    public Map<String,Object> getUserInfo(@PathVariable String token){
    	Object cache = getUserCache(token);
    	if (cache == null) {
    		return error();
    	}
    	Map<String,Object> map = success();
    	map.put("data", (User)cache);
    	return map;
    }
    
    /**
     * 用户忘记密码
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/forgetPassword")
    public Map<String,Object> forgetPassword(String smsCode, String telephone, String password){
    	User user = userService.findUserByTelephone(telephone);
    	if (user == null) {
			return error("该手机号尚未注册");
    	}
    	user.setPassword(MD5Util.encode(password));
    	//修改用户密码
    	userService.save(user);
    	cacheUser(user);
    	return success("密码修改成功");
    }
    
    /**
     * 用户注册
     * @param smsCode
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/customer/register")
    public Map<String,Object> customerRegister(String smsCode,@Valid User user){
    	String telePhone = user.getPhoneNum();
    	if (StringUtils.isEmpty(telePhone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(smsCode)) {
    		return error("验证码不能为空");
    	}
    	Object cache = getSmsCodeCache(telePhone);
    	if (cache == null) {
    		return error("验证码错误");
    	}
    	if (!((String)cache) .equals(smsCode)) {
    		return error("验证码错误");
    	}
    	if (StringUtil.isEmpty(user.getUserAccount())) {
    		return error("用户名不能为空");
    	}
    	user.setUserType(User.CUSTOMER);
    	user.generateUUID();
    	userService.save(user);
    	return success(user, "注册成功");
    }
     
    /**
     * 用户注册
     * @param smsCode
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/shoper/register")
    public Map<String,Object> shoperRegister(String smsCode,@Valid User user){
    	String telePhone = user.getPhoneNum();
    	if (StringUtils.isEmpty(telePhone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(smsCode)) {
    		return error("验证码不能为空");
    	}
    	Object cache = getSmsCodeCache(telePhone);
    	if (cache == null) {
    		return error("验证码错误");
    	}
    	if (!((String)cache).equals(smsCode)) {
    		return error("验证码错误");
    	}
    	if (StringUtil.isEmpty(user.getUserAccount())) {
    		return error("用户名不能为空");
    	}
    	
    	if (StringUtil.isEmpty(user.getShopName())) {
    		return error("商户名不能为空");
    	}
    	
    	if (StringUtil.isEmpty(user.getTrueName())) {
    		return error("联系人不能为空");
    	}
    	
    	if (StringUtil.isEmpty(user.getAddress())) {
    		return error("地理信息不能为空");
    	}
    	user.setUserType(User.SHOPER);
    	user.generateUUID();
    	userService.save(user);
    	return success(user, "注册成功");
    }
    
    /**
     * 获取验证码
     * @param telephone
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/sendSmsCode")
    public Map<String,Object> sendSmsCode(String telephone){
    	cacheSmsCode(telephone, "123456");
    	return success("验证码发送成功");
    }
    
    @ResponseBody
    @RequestMapping(value = "modifyNickName")
    @NeedAuth
    public Map<String,Object> modifyNickName(String nickName){
    	User user = getUser();
    	user.setNickName(nickName);
    	user.setUpdateTime(new Date());
    	userService.save(user);
    	return success();
    }
    
    //文件上传相关代码
    @ResponseBody
    @RequestMapping(value = "picture/upload")
    @NeedAuth
    public Map<String,Object> upload(@RequestParam("pictureFile") MultipartFile pictureFile) {
        if (pictureFile.isEmpty()) {
            return error("图片不能为空");
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
        	User user = getUser();
        	user.setImgUrl(picturePath + fileName);
        	user.setUpdateTime(new Date());
        	userService.save(user);
            return success("头像上传成功");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return error("服务器请假了，请稍后再试");
    }

}
