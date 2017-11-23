package com.gms.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 跨域请求拦截器，实现api接口跨域访问
 * 
 * @author xuyao
 *
 */
public class CORSInterceptor extends HandlerInterceptorAdapter {
	//文件存储路径
	@Value(value = "${model}")
	private String model;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if ("debug".equals(model)) {
			response.setHeader("Access-Control-Allow-Origin", "https://localhost");
		} else {
			String origin = request.getHeader("Origin");
			if (StringUtils.isEmpty(origin) && origin.endsWith(".lvmama.com"))
				response.setHeader("Access-Control-Allow-Origin", origin);
			else
				response.setHeader("Access-Control-Allow-Origin", "https://m.lvmama.com");
		}
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		return true;
	}
}
