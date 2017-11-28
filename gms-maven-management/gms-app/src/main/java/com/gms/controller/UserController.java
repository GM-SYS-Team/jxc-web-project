package com.gms.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.gms.annoation.NeedAuth;
import com.gms.entity.jxc.User;
import com.gms.service.jxc.UserService;
import com.gms.util.EmojiUtils;
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
    @RequestMapping("/login")
    public Map<String,Object> shoperLogin(String password, String telephone){
    	if (StringUtils.isEmpty(telephone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(password)) {
    		return error("密码不能为空");
    	}
    	telephone = telephone.replaceAll(" ", "");
    	User user = userService.findUserByTelephone(telephone, User.SHOPER);
    	if (user == null) {
			return error("用户名或者密码错误");
    	}
    	if (!user.getPassword().equals(MD5Util.encode(password))) {
    		return error("用户名或者密码错误");
    	}
    	cacheUser(user);
    	user.setPhoneNum(user.getPhoneNum().substring(0,3) + "****" + user.getPhoneNum().substring(7));
    	Map<String,Object> map = success();
    	map.put("data", user);
    	return map;
    }
    
	/**
     * 用户登录请求
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/customer/login")
    public Map<String,Object> login(String password, String telephone){
    	if (StringUtils.isEmpty(telephone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(password)) {
    		return error("密码不能为空");
    	}
    	telephone = telephone.replaceAll(" ", "");
    	User user = userService.findUserByTelephone(telephone, User.CUSTOMER);
    	if (user == null) {
			return error("用户名或者密码错误");
    	}
    	if (!user.getPassword().equals(MD5Util.encode(password))) {
    		return error("用户名或者密码错误");
    	}
    	cacheUser(user);
    	user.setPhoneNum(user.getPhoneNum().substring(0,3) + "****" + user.getPhoneNum().substring(7));
    	Map<String,Object> map = success();
    	map.put("data", user);
    	return map;
    }
    
    /**
     * 用户忘记密码
     * @param user
     * @return
     */
    @ResponseBody
    @RequestMapping("/forgetPassword")
    public Map<String,Object> forgetPassword(String smsCode, String telephone, String password, String userType){
    	if (StringUtils.isEmpty(telephone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(smsCode)) {
    		return error("验证码不能为空");
    	}
    	telephone = telephone.replaceAll(" ", "");
    	Object cache = getSmsCodeCache(telephone);
    	if (cache == null) {
    		return error("验证码错误");
    	}
    	if (!((String)cache) .equals(smsCode)) {
    		return error("验证码错误");
    	}
    	if (userType == null) {
    		return error("用户类型不能为空");
    	}
    	if (!(userType.equals(User.CUSTOMER) || userType.equals(User.SHOPER))) {
    		return error("用户类型参数错误");
    	}
    	User user = userService.findUserByTelephone(telephone, userType);
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
    @RequestMapping("/customer/register")
    public Map<String,Object> customerRegister(String smsCode, User user){
    	String telePhone = user.getPhoneNum();
    	if (StringUtils.isEmpty(telePhone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(smsCode)) {
    		return error("验证码不能为空");
    	}
    	telePhone = telePhone.replaceAll(" ", "");
    	Object cache = getSmsCodeCache(telePhone);
    	if (cache == null) {
    		return error("验证码错误");
    	}
    	if (!((String)cache) .equals(smsCode)) {
    		return error("验证码错误");
    	}
    	String password = user.getPassword();
    	if (StringUtil.isEmpty(password)) {
    		return error("密码不能为空");
    	}
    	user.setUserName(telePhone);
    	user.setPhoneNum(telePhone);
    	user.setUserType(User.CUSTOMER);
    	user.setPassword(MD5Util.encode(password));
    	user.generateUUID();
    	User tmp = userService.findUserByTelephone(telePhone, User.CUSTOMER);
    	if (tmp != null) {
    		return error("该手机号已经注册");
    	}
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
    @RequestMapping("/shoper/register")
    public Map<String,Object> shoperRegister(String smsCode, User user){
    	String telePhone = user.getPhoneNum();
    	if (StringUtils.isEmpty(telePhone)) {
    		return error("手机号不能为空");
    	}
    	if (StringUtils.isEmpty(smsCode)) {
    		return error("验证码不能为空");
    	}
    	telePhone = telePhone.replaceAll(" ", "");
    	Object cache = getSmsCodeCache(telePhone);
    	if (cache == null) {
    		return error("验证码错误");
    	}
    	if (!((String)cache).equals(smsCode)) {
    		return error("验证码错误");
    	}
    	String password = user.getPassword();
    	if (StringUtil.isEmpty(password)) {
    		return error("密码不能为空");
    	}
    	if (StringUtil.isEmpty(user.getTrueName())) {
    		return error("联系人不能为空");
    	}
    	if (StringUtil.isEmpty(user.getAddress())) {
    		return error("详细地址不能为空");
    	}
    	if (StringUtil.isEmpty(user.getDistrict())) {
    		return error("省区不能为空");
    	}
    	user.setUserName(telePhone);
    	user.setPhoneNum(telePhone);
    	user.setPassword(MD5Util.encode(password));
    	user.setUserType(User.SHOPER);
    	user.generateUUID();
    	User tmp = userService.findUserByTelephone(telePhone, User.SHOPER);
    	if (tmp != null) {
    		return error("该手机号已经注册");
    	}
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
    @RequestMapping("/sendSmsCode")
    public Map<String,Object> sendSmsCode(String telephone){
    	telephone = telephone.replaceAll(" ", "");
    	cacheSmsCode(telephone, "123456");
    	return success("验证码发送成功");
    }
    
    @ResponseBody
    @RequestMapping("/modifyNickName")
    @NeedAuth
    public Map<String,Object> modifyNickName(String nickName){
    	User user = getUser();
    	user.setNickName(EmojiUtils.filter(nickName));
    	user.setUpdateTime(new Date());
    	userService.save(user);
    	cacheUser(user);
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
