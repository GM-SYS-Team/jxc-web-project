package com.gms.controller;

import com.gms.entity.jxc.User;
import com.gms.util.CacheUtil;
import com.gms.util.Constant;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class BaseAppController {

    /**
     * request对象
     */
    protected ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();

    /**
     * response对象
     */
    protected ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    /**
     * 获取request对象
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return requestThreadLocal.get();
    }

    /**
     * 获取response对象
     *
     * @return
     */
    public HttpServletResponse getResponse() {
        return responseThreadLocal.get();
    }

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        requestThreadLocal.set(request);
        responseThreadLocal.set(response);
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    User getUser() {
        User user = (User) getUserCache(getRequest().getParameter("access_token"));
        return user;

    }

    void cacheUser(User user) {
        CacheUtil.put(CacheUtil.USER, user.getUuid(), new User(user));
    }

    Object getUserCache(String key) {
        return CacheUtil.get(CacheUtil.USER, key);
    }

    void cacheSmsCode(String key, String value) {
        CacheUtil.put(CacheUtil.SMS_CODE, key, value);
    }

    Object getSmsCodeCache(String key) {
        return CacheUtil.get(CacheUtil.SMS_CODE, key);
    }

    /**
     * 返回成功的说明
     *
     * @param data
     * @param message
     * @return
     */
    Map<String, Object> success(Object data, String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_SUCCESS);
        map.put("data", data);
        map.put("msg", message);
        return map;
    }

    /**
     * 返回成功的说明
     *
     * @param data
     * @param message
     * @return
     */
    Map<String, Object> success(Object data) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_SUCCESS);
        map.put("data", data);
        map.put("msg", "操作成功");
        return map;
    }

    Map<String, Object> success(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_SUCCESS);
        map.put("msg", message);
        return map;
    }

    Map<String, Object> success() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_SUCCESS);
        map.put("msg", "操作成功");
        return map;
    }

    Map<String, Object> error(String message) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_ERROR);
        map.put("msg", message);
        return map;
    }

    Map<String, Object> error() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", Constant.RET_CODE_ERROR);
        map.put("msg", "操作失败");
        return map;
    }


}
