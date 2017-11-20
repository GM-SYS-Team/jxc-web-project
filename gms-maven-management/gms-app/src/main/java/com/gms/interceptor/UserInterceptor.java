package com.gms.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.gms.annoation.NeedAuth;
import com.gms.service.jxc.UserService;
import com.gms.util.CacheUtil;

public class UserInterceptor extends HandlerInterceptorAdapter {
	
	@Resource
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			NeedAuth methodNeedAuth = handlerMethod.getMethod().getAnnotation(NeedAuth.class);
			NeedAuth classNeedAuth = handlerMethod.getBeanType().getAnnotation(NeedAuth.class);
			// 签名校验
			if (methodNeedAuth != null || classNeedAuth != null){
				String accessToken = request.getParameter("access_token");
				if (StringUtils.isEmpty(accessToken)) {
		            response.setContentType("application/json;charset=UTF-8");
		            response.getWriter().write("{\"code\":2,\"msg\":\"请登录再操作\"}");
		            return false;
				}
				else {
					Object user = CacheUtil.get(CacheUtil.USER, accessToken);
					if (user == null) {
						user = userService.findByUuid(accessToken);
						if (user == null) {
							response.setContentType("application/json;charset=UTF-8");
				            response.getWriter().write("{\"code\":2,\"msg\":\"请登录再操作\"}");
				            return false;
						}
						CacheUtil.put(CacheUtil.USER, accessToken, user);
					}
				}
			}			
		}
		return true;
	}

}
