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
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		if (request.getMethod().equalsIgnoreCase("options")) {
			response.setStatus(200);
			response.getWriter().println("OK");
			return false;
		}
		return true;
	}
}
