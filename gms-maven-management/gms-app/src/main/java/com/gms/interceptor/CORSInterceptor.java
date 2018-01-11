package com.gms.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 跨域请求拦截器，实现api接口跨域访问
 * 
 */
public class CORSInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Credentials","true");
		response.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers","Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
		
		if(request.getMethod().equalsIgnoreCase("options")){
			response.setStatus(200);
			response.getWriter().println("ok");
			return false;
		}
		return true;
	}
}
